package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger

import org.firstinspires.ftc.robotcore.internal.webserver.websockets.CloseCode
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketServer
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketManagerImpl
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketNamespaceHandlerRegistry
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level.FINE
import java.util.logging.LogRecord
import java.util.logging.Logger

class RDWebSocketServer(address: InetSocketAddress?) :
    WebSocketServer(address, DECODER_THREAD_COUNT),
    FtcWebSocketServer
{

    private val manager = WebSocketManagerImpl()
    private val wsMap: MutableMap<WebSocket, RDWebSocket> = ConcurrentHashMap()
    private val logger = Logger.getLogger(this::class.java.name)

    init {
        isReuseAddr = true
        connectionLostTimeout = 5
        WebSocketNamespaceHandlerRegistry.onWebSocketServerCreation(manager)
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        val webSocket = RDWebSocket(
            conn,
            port, conn.remoteSocketAddress.address, conn.remoteSocketAddress.hostName, manager
        )
        wsMap[conn] = webSocket
        webSocket.onOpen()
        for (message in initBroadcasts) webSocket.send(message)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        wsMap[conn]!!
            .onClose(CloseCode.find(code), reason, remote)
    }

    override fun onMessage(conn: WebSocket, message: String) {

        for (ws in wsMap.values) ws.onMessage(message)
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        if (conn != null) {
            wsMap[conn]!!.onException(ex)
        } else {
            logger.severe("WebSocket server error $ex")
        }
    }

    @Throws(IllegalArgumentException::class)
    fun enableMotor(number: Int) {
        if (number > 7 || 0 > number) {
            throw IllegalArgumentException("enableMotor must be between 0 and 7")
        }

        broadcast(motorEnableMessage(number))
        initBroadcasts.add(motorEnableMessage(number))
    }

    @Throws(IllegalArgumentException::class)
    fun enableServo(number: Int) {
        if (number > 10 || 0 > number) {
            throw IllegalArgumentException("enableServo must be between 0 and 10")
        }

        broadcast(servoEnableMessage(number))
        initBroadcasts.add(servoEnableMessage(number))
    }

    override fun onStart() {
        logger.log(LogRecord(FINE, "Remote debugger started on port $port"))
        AdvancedLogging.logText("Started Web Server")
        AdvancedLogging.updateLog()
    }

    override fun getWebSocketManager(): WebSocketManager {
        return manager
    }

    /**
     * Broadcasts all relevant data to connected clients
     */
    fun updateData() {
        // Update all enabled motors
        for (i in 0..7) {
            broadcast(motorEnableMessage(i, enabledMotors[i]))
        }

        // Update all motor power values
        for (i in 0..7) {
            broadcast(motorPowerMessage(i, motorPowers[i]))
        }

        // Update all enabled servos
        for (i in 0..10) {
            broadcast(servoEnableMessage(i, enabledServos[i]))
        }

        // Update all servo positions
        for (i in 0..10) {
            broadcast(servoPositionMessage(i, servoPositions[i]))
        }
    }

    companion object {
        const val DECODER_THREAD_COUNT = 1
        private val initBroadcasts: MutableList<String> = mutableListOf()
        val motorPowers = Array(8) { 0.0 }
        val enabledMotors = Array(8) { false }
        val servoPositions = Array(10) { 0.0 }
        val enabledServos = Array(10) { false }

        @JvmStatic
        fun initializeWebsocketServer(): RDWebSocketServer {
            val server = RDWebSocketServer(InetSocketAddress(50000))
            server.start()
            return server
        }

        /**
         * Tell the client we can use a specific motor.
         * Be careful, as the static method does _**not**_ broadcast
         * the message to existing websockets,
         * only to new ones.
         *
         * @param number The motor number to enable
         * @throws IllegalArgumentException If the number is not between 0 and 7
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun enableMotorStatic(number: Int) {
            if (number > 7 || 0 > number) {
                throw IllegalArgumentException("enableMotor must be between 0 and 7")
            }

            enabledMotors[number] = true
        }

        /**
         * Tell the client we can use specific motors.
         * Be careful, as the static method does _**not**_ broadcast
         * the message to existing websockets,
         * only to new ones.
         * Enables range from number to numberTo inclusive
         *
         * @param number The first motor number to enable
         * @param numberTo The last motor number to enable
         * @throws IllegalArgumentException If the number is not between 0 and 7
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun enableMotorStatic(number: Int, numberTo: Int) {
            for (i in number..numberTo) {
                enableMotorStatic(i)
            }
        }

        /**
         * Tell the client we can use a specific servo.
         * Be careful, as the static method does _**not**_ broadcast
         * the message to existing websockets,
         * only to new ones.
         *
         * @param number The servo number to enable
         * @throws IllegalArgumentException If the number is not between 0 and 5
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun enableServoStatic(number: Int) {
            if (number > 10 || 0 > number) {
                throw IllegalArgumentException("enableServo must be between 0 and 5")
            }

            enabledServos[number] = true
        }

        /**
         * Tell the client we can use specific servos.
         * Be careful, as the static method does _**not**_ broadcast
         * the message to existing websockets,
         * only to new ones.
         * Enables range from number to numberTo inclusive
         *
         * @param number The first servo number to enable
         * @param numberTo The last servo number to enable
         * @throws IllegalArgumentException If the number is not between 0 and 5
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun enableServoStatic(number: Int, numberTo: Int) {
            for (i in number..numberTo) {
                enableServoStatic(i)
            }
        }

        /**
         * Creates a message to enable a motor
         * @param number The motor number to enable
         * @param speed The speed to set the motor to
         *
         * @throws IllegalArgumentException If the number is not between 0 and 7 or the speed is not between -1 and 1
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun setMotorPowerStatic(number: Int, speed: Double) {
            if (number > 7 || 0 > number) {
                throw IllegalArgumentException("number must be between 0 and 7")
            }

            motorPowers[number] = speed
        }

        /**
         * Creates a message to enable a servo
         * @param number The servo number to enable
         * @param position The speed to set the servo to
         *
         * @throws IllegalArgumentException If the number is not between 0 and 5 or the speed is not between 0 and 100
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun setServoPositionStatic(number: Int, position: Double) {
            if (number > 10 || 0 > number) {
                throw IllegalArgumentException("number must be between 0 and 10")
            }

            servoPositions[number] = position
        }
    }
}
