package org.firstinspires.ftc.teamcode.autonomous.localization.localizers

import org.firstinspires.ftc.teamcode.autonomous.localization.PoseTimePair
import org.firstinspires.ftc.teamcode.autonomous.localization.RobotPose

/**
 * Represents a system capable of determining the pose of the robot in the field coordinate system.
 * The suggested implementation is to add getters and setters for the `pose` and `velocity` properties.
 */
interface Localizer {
    /**
     * The pose of the robot in the field coordinate system.
     *
     * Can be null if the pose is not known.
     */
    var pose: RobotPose?

    /**
     * Get the velocity of the robot.
     *
     * Can be null if the localizer does not support velocity or if the velocity is not known.
     */
    val velocity: RobotPose?
}