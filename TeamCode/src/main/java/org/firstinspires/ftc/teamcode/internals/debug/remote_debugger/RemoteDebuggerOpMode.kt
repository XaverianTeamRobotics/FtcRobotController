package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger

import org.firstinspires.ftc.teamcode.internals.debug.Debugger
import org.firstinspires.ftc.teamcode.internals.debug.remote_debugger.RDWebSocketServer.Companion.enableMotorStatic
import org.firstinspires.ftc.teamcode.internals.debug.remote_debugger.RDWebSocketServer.Companion.enableServoStatic
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging

class RemoteDebuggerOpMode : OperationMode(), TeleOperation {
    override fun construct() {
        Logging.clear()
        enableMotorStatic(0, 3)
        enableServoStatic(0, 5)

        registerFeature(Debugger())
    }

    override fun run() {
    }
}
