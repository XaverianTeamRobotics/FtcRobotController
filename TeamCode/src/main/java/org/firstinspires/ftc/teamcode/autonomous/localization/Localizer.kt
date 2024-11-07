package org.firstinspires.ftc.teamcode.autonomous.localization

/**
 * Represents a system capable of determining the pose of the robot in the field coordinate system.
 * The suggested implementation is to add getters and setters for the `pose` property and keep a history of poses.
 * <br>
 * This is a very abstract interface and can be implemented in many ways, such as using odometry, vision, etc
 */
interface Localizer {
    /**
     * The pose of the robot in the field coordinate system.
     * A getter will lazily calculate the pose
     * A setter will handle manual recalibrations
     */
    var pose: RobotPose?

    /**
     * A list of all the poses of the robot in the field coordinate system at different times.
     * The list should be ordered by time, earliest first.
     * If the pose history is manually updated, this list should be cleared and recalculated.
     */
    val poseHistory: List<PoseTimePair>
}