package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.EncoderDirection.FORWARD
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.EncoderDirection.REVERSED
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_X_OFFSET
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_X_REVERSED
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_Y_OFFSET
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.PINPOINT_Y_REVERSED

class PinpointLocalizer: Localizer {
    override var poseEstimate: Pose2d = Pose2d()
        set(value) {
            field = value
            pinpoint.position = Pose2d(value.x * 25.4, value.y * 25.4, Math.toRadians(value.heading))
        }

    override var poseVelocity: Pose2d? = null

    val pinpoint = HardwareManager.pinpoint

    init {
        try {
            pinpoint.manufacturer
        } catch (_: Exception) {
            throw Exception("GoBilda Pinpoint not found")
        }

        pinpoint.setOffsets(PINPOINT_X_OFFSET, PINPOINT_Y_OFFSET)
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        pinpoint.setEncoderDirections(if (PINPOINT_X_REVERSED) REVERSED else FORWARD, if (PINPOINT_Y_REVERSED) REVERSED else FORWARD)
        pinpoint.resetPosAndIMU()
        // Wait 0.25 seconds for the IMU to calibrate
        // The wait may not be perfect, but it's an approximation.
        // Because this should take place during init, we should in theory be fine
        Thread.sleep(250)
    }

    override fun update() {
        pinpoint.update()
        val rawP = pinpoint.position
        val rawV = pinpoint.velocity

        // pinpoint units are in mm and radians, need to convert to inches and degrees
        poseEstimate = Pose2d(rawP.x / 25.4, rawP.y / 25.4, Math.toDegrees(rawP.heading))
        poseVelocity = Pose2d(rawV.x / 25.4, rawV.y / 25.4, Math.toDegrees(rawV.heading))
    }
}