package org.firstinspires.ftc.teamcode.autonomous.localization

import org.firstinspires.ftc.teamcode.internals.Vector
import org.firstinspires.ftc.teamcode.internals.units.AngleUnit
import org.firstinspires.ftc.teamcode.internals.units.LengthUnit

/**
 * Represents a pair of a pose and a velocity.
 * @property pose The pose of the robot
 * @property velocity The velocity of the robot, using the same coordinate system as the pose
 */
data class PoseVelocityPair(val pose: RobotPose, val velocity: RobotPose)

/**
 * Represents the pose of the robot in the field coordinate system.
 * @property x The x coordinate of the robot in the field coordinate system. X values start at the center and
 * positive X is away from the audience
 * @property y The y coordinate of the robot in the field coordinate system. Y values start at the center and
 * positive Y is towards the blue alliance
 * @property heading The heading of the robot in the field coordinate system. Heading is in degrees and is
 * measured counter-clockwise from the positive X axis
 */
data class RobotPose(val x: LengthUnit, val y: LengthUnit, val heading: AngleUnit) {
    operator fun plus(other: RobotPose) = RobotPose(x + other.x, y + other.y, heading + other.heading)
    operator fun minus(other: RobotPose) = RobotPose(x - other.x, y - other.y, heading - other.heading)

    fun toVector() = Vector(x, y)

    override fun toString() = "RobotPose(x=$x, y=$y, heading=$heading)"
}
