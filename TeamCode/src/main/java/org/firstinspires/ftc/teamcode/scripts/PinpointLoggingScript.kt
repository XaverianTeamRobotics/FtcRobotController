package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.autonomous.localizers.PinpointLocalizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.templates.Script

class PinpointLoggingScript: Script() {
    val localizer = PinpointLocalizer()

    override fun init() {

    }

    override fun run() {
        while (scriptIsActive()) {
            localizer.update()
            telemetry.addData("X", localizer.poseEstimate.x)
            telemetry.addData("Y", localizer.poseEstimate.y)
            telemetry.addData("Heading", localizer.poseEstimate.heading)
            telemetry.update()
        }
    }

    override fun onStop() {

    }
}