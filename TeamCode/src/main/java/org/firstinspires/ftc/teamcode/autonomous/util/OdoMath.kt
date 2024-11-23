package org.firstinspires.ftc.teamcode.autonomous.util

import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings

fun rpmToVelocity(rpm: Double): Double {
    val gearRatio = AutoSettings.GEAR_RATIO
    val wheelRadius = AutoSettings.WHEEL_RADIUS

    return rpm * 2.0 * Math.PI * wheelRadius * gearRatio / 60.0
}