package org.firstinspires.ftc.teamcode.scripts.auto

import org.firstinspires.ftc.teamcode.autonomous.localization.localizers.GoBildaPinpointLocalizer
import org.firstinspires.ftc.teamcode.autonomous.localization.localizers.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_X_OFFSET
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_Y_OFFSET
import org.firstinspires.ftc.teamcode.internals.templates.Script
import org.firstinspires.ftc.teamcode.internals.units.inches

class GoBildaPinpointLogging: Script() {
    lateinit var localizer: GoBildaPinpointLocalizer

    override fun init() {
        localizer = GoBildaPinpointLocalizer(PINPOINT_X_OFFSET.inches, PINPOINT_Y_OFFSET.inches, directionX = GoBildaPinpointDriver.EncoderDirection.REVERSED)
        telemetry.addLine("VERIFY PINPOINT SHOWS GREEN LIGHT")
        telemetry.update()
    }

    override fun run() {
        while (scriptIsActive()) {
            val p = localizer.pose
            val v = localizer.velocity
            telemetry.addData("Pose", p)
            telemetry.addData("Velocity", v)
            telemetry.update()
        }
    }

    override fun onStop() {

    }
}