package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.autonomous.limelight.LimelightServoScript
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import java.lang.Math.toRadians
import kotlin.math.pow
import kotlin.math.sqrt

class LimelightLocalizer: Localizer {
    init {
        if (limelight3A == null) throw RuntimeException("Limelight not found")
        telemetry.msTransmissionInterval = 11
        limelight3A!!.pipelineSwitch(AutoSettings.LIMELIGHT_MT_PIPELINE_ID.toInt())
        limelight3A!!.start()
    }

    override var poseEstimate: Pose2d = Pose2d()
    override val poseVelocity: Pose2d? = null

    var headingProvider: (() -> Double)? = null

    override fun update() {
        if (!disabled) {
            if (headingProvider != null) {
                limelight3A!!.updateRobotOrientation(headingProvider!!())
            }
            val result = limelight3A!!.latestResult
            if (result != null && result.isValid && result.fiducialResults.isNotEmpty()) {
                val botpose = if (headingProvider != null) result.botpose_MT2 else result.botpose
                // M to in = 39.37008
                poseEstimate = Pose2d(
                    botpose.position.x * 39.37008,
                    botpose.position.y * 39.37008,
                    toRadians(botpose.orientation.yaw)
                )
            } else {
                poseEstimate = HybridLocalizer.NULL_POSE
            }
        }
    }

    companion object {
        var disabled: Boolean = false

        /**
         * For communication between this and LimelightServoScript. Does not actually move the servo
         */
        var servoPos: LimelightServoScript.LimelightServoPosition = LimelightServoScript.LimelightServoPosition.CENTER
    }
}