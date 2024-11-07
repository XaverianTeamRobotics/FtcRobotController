package org.firstinspires.ftc.teamcode.autonomous.localization

/**
 * Represents a pair of a pose and a time.
 * @property pose The pose of the robot at the given time
 * @property time The time since the start of the autonomous period when the pose was recorded
 */
data class PoseTimePair(val pose: RobotPose, val time: Int)
