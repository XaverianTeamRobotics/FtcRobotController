package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import java.lang.Math.pow
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

    override fun update() {
        val result = limelight3A!!.latestResult
        if (result != null && result.isValid) {
            // Find the closest marker. If it is more than 72 inches away, set poseEstimate to NULL_POSE
            var closest = Double.MAX_VALUE
            for (marker in result.fiducialResults) {
                val p = marker.targetPoseCameraSpace.position
                val d = sqrt(p.x.pow(2) + p.y.pow(2) + p.z.pow(2))
                if (d < closest) closest = d
            }
            if (closest > 72) {
                poseEstimate = HybridLocalizer.NULL_POSE
                return
            }
            val botpose = result.botpose
            // M to in = 39.37008
            poseEstimate = Pose2d(botpose.position.x * 39.37008, botpose.position.y * 39.37008, botpose.orientation.yaw)
        } else {
            poseEstimate = HybridLocalizer.NULL_POSE
        }
    }
}