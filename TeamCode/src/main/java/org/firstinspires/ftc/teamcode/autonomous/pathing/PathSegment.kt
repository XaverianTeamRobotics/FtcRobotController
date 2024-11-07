package org.firstinspires.ftc.teamcode.autonomous.pathing

import org.firstinspires.ftc.teamcode.autonomous.localization.PoseVelocityPair
import org.firstinspires.ftc.teamcode.autonomous.localization.RobotPose

/**
 * Represents a segment of a path that the robot can follow.
 */
interface PathSegment {
    /**
     * The start pose of the path segment
     */
    val start: PoseVelocityPair

    /**
     * The end pose of the path segment
     */
    val end: PoseVelocityPair

    /**
     * Get the robot pose at a certain displacement along the path segment
     * @param displacement The displacement from the start of this path segment
     * @return The desired pose and velocity of the robot at the given displacement
     */
    fun getAtDisplacement(displacement: Double): RobotPose

    /**
     * Get the desired velocity of the robot at a certain time
     * @param time The current time in ms from the start of this path segment
     * @param currentPoseVelocity The current pose and velocity of the robot
     * @return The desired velocity of the robot at the given time
     */
    fun getAtTime(time: Int, currentPoseVelocity: PoseVelocityPair): RobotPose

    /**
     * Set the start velocity of the path segment
     * @param velocity The desired velocity of the robot at the start of the path segment
     */
    fun setStartVelocity(velocity: RobotPose)

    /**
     * Set the end velocity of the path segment
     * @param velocity The desired velocity of the robot at the end of the path segment
     */
    fun setEndVelocity(velocity: RobotPose)
}