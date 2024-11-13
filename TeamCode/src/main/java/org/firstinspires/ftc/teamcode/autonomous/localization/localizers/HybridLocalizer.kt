package org.firstinspires.ftc.teamcode.autonomous.localization.localizers

import org.firstinspires.ftc.teamcode.autonomous.localization.RobotPose

class HybridLocalizer(val primary: Localizer, val fallback: Localizer): Localizer {
    override var pose: RobotPose?
        get() =
            if (primary.pose != null) {
                val p = primary.pose
                fallback.pose = p
                p
            } else {
                fallback.pose
            }
        set(value) {
            primary.pose = value
            fallback.pose = value
        }
    override val velocity: RobotPose?
        get() =
            if (primary.velocity != null) {
                primary.velocity
            } else {
                fallback.velocity
            }
}