package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.EncoderDirection.FORWARD
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.EncoderDirection.REVERSED
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_X_OFFSET
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_X_REVERSED
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_YAW_SCALAR
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_Y_OFFSET
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_Y_REVERSED

class PinpointLocalizer: Localizer {
    private var _pe: Pose2d = Pose2d()

    override var poseEstimate: Pose2d
        get() = _pe
        set(value) {
            _pe = value
            pinpoint.position = value.copy(value.x * 25.4, value.y * 25.4)
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
        if (PINPOINT_YAW_SCALAR != 0.0) {
            pinpoint.setYawScalar(PINPOINT_YAW_SCALAR * pinpoint.getYawScalar())
        }
        pinpoint.setEncoderDirections(if (PINPOINT_X_REVERSED) REVERSED else FORWARD, if (PINPOINT_Y_REVERSED) REVERSED else FORWARD)
        pinpoint.resetPosAndIMU()
        // Wait 0.25 seconds for the IMU to calibrate
        // The wait may not be perfect, but it's an approximation.
        // Because this should take place during init, we should in theory be fine
        Thread.sleep(250)
    }

    override fun update() {
        pinpoint.update()

        // pinpoint units are in mm, need to convert to inches
        _pe = pinpoint.position.copy(x = pinpoint.position.x / 25.4, y = pinpoint.position.y / 25.4)
        poseVelocity = pinpoint.velocity.copy(x = pinpoint.velocity.x / 25.4, y = pinpoint.velocity.y / 25.4)
    }
}