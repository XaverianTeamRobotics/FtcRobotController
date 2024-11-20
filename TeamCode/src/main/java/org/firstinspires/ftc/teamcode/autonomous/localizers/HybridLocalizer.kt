package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer

class HybridLocalizer(val primary: Localizer, val fallback: Localizer): Localizer {
    override var poseEstimate: Pose2d = Pose2d()
        set(value) {
            field = value
            primary.poseEstimate = value
            fallback.poseEstimate = value
        }

    override var poseVelocity: Pose2d? = null

    override fun update() {
        primary.update()
        fallback.update()

        if (!(primary.poseEstimate epsilonEquals NULL_POSE)) {
            poseEstimate = primary.poseEstimate
            fallback.poseEstimate = primary.poseEstimate
        } else {
            poseEstimate = fallback.poseEstimate
            primary.poseEstimate = fallback.poseEstimate
        }

        poseVelocity = if (primary.poseVelocity != null) {
            primary.poseVelocity
        } else {
            fallback.poseVelocity
        }
    }

    companion object {
        val NULL_POSE = Pose2d(-9999.0, -9999.0, -9999.0)
    }
}

infix fun Localizer.hybrid(fallback: Localizer) = HybridLocalizer(this, fallback)