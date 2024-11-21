package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings

class LimelightLocalizer : Localizer {
    init {
        if (limelight3A == null) throw RuntimeException("Limelight not found")
        telemetry.msTransmissionInterval = 11
        limelight3A!!.pipelineSwitch(OdometrySettings.LIMELIGHT_MT_PIPELINE_ID.toInt())
        limelight3A!!.start()
    }

    override var poseEstimate: Pose2d = Pose2d()
    override val poseVelocity: Pose2d? = null

    override fun update() {
        val result = limelight3A!!.latestResult
        if (result != null && result.isValid) {
            val botpose = result.botpose
            // M to in = 39.37008
            poseEstimate = Pose2d(botpose.position.x * 39.37008, botpose.position.y * 39.37008, botpose.orientation.yaw)
        } else {
            poseEstimate = HybridLocalizer.NULL_POSE
        }
    }
}