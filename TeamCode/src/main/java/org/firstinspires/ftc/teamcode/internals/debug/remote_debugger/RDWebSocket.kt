package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.CloseCode
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketImpl.RawWebSocket
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.java_websocket.WebSocket
import org.java_websocket.exceptions.WebsocketNotConnectedException
import java.net.InetAddress
import java.util.logging.Logger

class RDWebSocket(
    val webSocket: WebSocket?,
    port: Int,
    remoteIpAddress: InetAddress?,
    remoteHostname: String?,
    webSocketManager: WebSocketManager?
) : RawWebSocket(
    port,
    remoteIpAddress,
    remoteHostname,
    webSocketManager
) {
    private val logger = Logger.getLogger(this::class.java.name)

    override fun isOpen(): Boolean {
        return webSocket!!.isOpen
    }

    public override fun send(payload: String?) {
        try {
            webSocket!!.send(payload)
        } catch (e: WebsocketNotConnectedException) {
            logger.warning("Attempted to send message to unconnected WebSocket")
        }
    }

    override fun close(closeCode: Int, reason: String?) {
        webSocket!!.close(closeCode, reason)
    }

    override fun onMessage(message: String?) {
        val messageSerialized = Json.parseToJsonElement(message!!)
        Logging.log(message)
        Logging.update()
        logger.fine("Received message: $messageSerialized")

        val type: String = messageSerialized.jsonObject["type"]!!.jsonPrimitive.content

        when (type) {
            "ping" -> send("pong")
            "set-motor-power" -> motorPowerCallback(
                messageSerialized.jsonObject["motor"]!!.jsonPrimitive.content.toInt(),
                messageSerialized.jsonObject["power"]!!.jsonPrimitive.content.toDouble()
            )
            "telemetry-print" -> logCallback(messageSerialized.jsonObject["content"]!!.jsonPrimitive.content)
        }
    }

    override fun onOpen() {
        super.onOpen()

        // Send a welcome message
        send(welcomeMessage())
    }

    override fun onClose(closeCode: CloseCode?, reason: String?, initiatedByRemote: Boolean) {
        super.onClose(closeCode, reason, initiatedByRemote)
    }
}