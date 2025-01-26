package org.firstinspires.ftc.teamcode.autonomous.localizers

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.MAX_SAFE_LINEAR_VELOCITY
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class HybridLocalizer(val primary: Localizer, val fallback: Localizer, private val enableMaxSafeVel: Boolean = true, var copyHeading: Boolean = false): Localizer {
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
        fallback.update()
        primary.update()

        if (!(primary.poseEstimate epsilonEquals NULL_POSE) && (!enableMaxSafeVel || (abs(fallback.poseVelocity!!.heading) < AutoSettings.MAX_SAFE_ANGULAR_VELOCITY && sqrt(fallback.poseVelocity!!.x.pow(2) + fallback.poseVelocity!!.y.pow(2)) < MAX_SAFE_LINEAR_VELOCITY))) {
            pe = primary.poseEstimate // Use primary
            var p: Pose2d = pe
            if (!copyHeading) {
                p = pe.copy(heading = fallback.poseEstimate.heading)
            }
            fallback.poseEstimate = p
            usingFallback = false
        } else {
            pe = fallback.poseEstimate // Use fallback
            var p: Pose2d = pe
            if (!copyHeading) {
                p = pe.copy(heading = primary.poseEstimate.heading)
            }
            primary.poseEstimate = p
            usingFallback = true
        }

        poseVelocity = if (primary.poseVelocity != null) {
            primary.poseVelocity
        } else {
            fallback.poseVelocity
        }

        if (enableLogging) {
            telemetry.addData("Using fallback", usingFallback)
            telemetry.update()
        }
    }

    companion object {
        val NULL_POSE = Pose2d(-9999.0, -9999.0, -9999.0)

        private var instance: HybridLocalizer? = null

        var enableLogging: Boolean = false
    }
}

infix fun Localizer.hybrid(fallback: Localizer) = HybridLocalizer(this, fallback)