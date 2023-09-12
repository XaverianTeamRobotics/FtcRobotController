package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger

import org.firstinspires.ftc.robotcore.internal.webserver.websockets.CloseCode
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketServer
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketManagerImpl
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.WebSocketNamespaceHandlerRegistry
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging
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
        wsMap[conn]!!.onMessage(message)
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
    }

    companion object {
        const val DECODER_THREAD_COUNT = 1
        private val initBroadcasts: MutableList<String> = mutableListOf()
        val motorPowers = Array(8) { 0.0 }
        val enabledMotors = Array(8) { false }

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
    }
}
