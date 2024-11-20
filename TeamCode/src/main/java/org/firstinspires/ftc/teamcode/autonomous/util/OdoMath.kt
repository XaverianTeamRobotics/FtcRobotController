package org.firstinspires.ftc.teamcode.autonomous.util

import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings

fun rpmToVelocity(rpm: Double): Double {
    val gearRatio = OdometrySettings.GEAR_RATIO
    val wheelRadius = OdometrySettings.WHEEL_RADIUS

    return rpm * 2.0 * Math.PI * wheelRadius * gearRatio / 60.0
}