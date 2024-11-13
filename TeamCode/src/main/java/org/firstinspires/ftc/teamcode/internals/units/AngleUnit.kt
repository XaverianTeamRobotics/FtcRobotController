package org.firstinspires.ftc.teamcode.internals.units

import kotlin.math.cos
import kotlin.math.sin

/**
 * A class which allows a number to be converted between different units of angle.
 */
class AngleUnit {
    companion object {
        const val RAD = 1.0
        const val DEG = 180 / Math.PI
    }

    // A set of properties which use the radians value to calculate the other units
    var radians: Double = 0.0

    var degrees: Double
        get() = radians * DEG
        set(value) {
            radians = value / DEG
        }

    val cos: Double
        get() = cos(radians)

    val sin: Double
        get() = sin(radians)

    operator fun plus(other: AngleUnit) = AngleUnit().apply {
        radians = radians + other.radians
    }

    operator fun minus(other: AngleUnit) = AngleUnit().apply {
        radians = radians - other.radians
    }

    operator fun compareTo(other: AngleUnit) = radians.compareTo(other.radians)

    operator fun unaryMinus() = AngleUnit().apply {
        radians = -radians
    }

    override fun toString(): String {
        return "$degrees deg"
    }
}

// A set of extension properties for int and double to convert between units of angle
val Int.radians: AngleUnit
    get() {
        val angleUnit = AngleUnit()
        angleUnit.radians = this.toDouble()
        return angleUnit
    }

val Int.degrees: AngleUnit
    get() {
        val angleUnit = AngleUnit()
        angleUnit.degrees = this.toDouble()
        return angleUnit
    }

val Double.radians: AngleUnit
    get() {
        val angleUnit = AngleUnit()
        angleUnit.radians = this
        return angleUnit
    }

val Double.degrees: AngleUnit
    get() {
        val angleUnit = AngleUnit()
        angleUnit.degrees = this
        return angleUnit
    }