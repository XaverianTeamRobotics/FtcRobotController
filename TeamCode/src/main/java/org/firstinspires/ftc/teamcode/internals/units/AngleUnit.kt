package org.firstinspires.ftc.teamcode.internals.units

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
        set(value) {
            field = value
            degrees = value * DEG
        }

    var degrees: Double = 0.0
        set(value) {
            field = value
            radians = value / DEG
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