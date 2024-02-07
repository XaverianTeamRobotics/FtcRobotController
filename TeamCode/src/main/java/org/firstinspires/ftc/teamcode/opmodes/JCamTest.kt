package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.JCam
import org.firstinspires.ftc.teamcode.features.JCam.Companion.complete
import org.firstinspires.ftc.teamcode.features.JCam.Companion.down
import org.firstinspires.ftc.teamcode.features.JCam.Companion.toggle
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging

class JCamTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(JCam())
    }

    override fun run() {
        if (complete()) {
            DSLogging.log(if (down()) "down" else "up")
            DSLogging.update()
            toggle()
        }
    }
}
