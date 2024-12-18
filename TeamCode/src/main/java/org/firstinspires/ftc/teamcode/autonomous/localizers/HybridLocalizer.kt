package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.MAX_SAFE_LINEAR_VELOCITY
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class HybridLocalizer(val primary: Localizer, val fallback: Localizer, private val enableMaxSafeVel: Boolean = true): Localizer {
    private var pe: Pose2d = Pose2d()

    override var poseEstimate: Pose2d
        get() = pe
        set(value) {
            pe = value
            primary.poseEstimate = value
            fallback.poseEstimate = value
        }

    override var poseVelocity: Pose2d? = null

    var usingFallback: Boolean = false
        private set

    override fun update() {
        primary.update()
        fallback.update()

        if (!(primary.poseEstimate epsilonEquals NULL_POSE) && (!enableMaxSafeVel || (abs(fallback.poseVelocity!!.heading) < AutoSettings.MAX_SAFE_ANGULAR_VELOCITY && sqrt(fallback.poseVelocity!!.x.pow(2) + fallback.poseVelocity!!.y.pow(2)) < MAX_SAFE_LINEAR_VELOCITY))) {
            pe = primary.poseEstimate // Use primary
            fallback.poseEstimate = primary.poseEstimate
            usingFallback = false
        } else {
            pe = fallback.poseEstimate // Use fallback
            primary.poseEstimate = fallback.poseEstimate
            usingFallback = true
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