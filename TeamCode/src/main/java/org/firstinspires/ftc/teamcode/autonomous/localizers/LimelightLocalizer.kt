package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings

class LimelightLocalizer : Localizer {
    init {
        limelight3A!!.pipelineSwitch(OdometrySettings.LIMELIGHT_MT_PIPELINE_ID.toInt())
        limelight3A!!.start()
    }

    override var poseEstimate: Pose2d = Pose2d()
    override val poseVelocity: Pose2d? = null

    override fun update() {
        val result = limelight3A!!.latestResult
        if (result.isValid) {
            val botpose = result.botpose
            poseEstimate = Pose2d(botpose.position.x, botpose.position.y, botpose.orientation.yaw)
        }
    }
}