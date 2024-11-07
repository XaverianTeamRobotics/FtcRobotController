package org.firstinspires.ftc.teamcode.internals.units

/**
 * A class which allows a number to be converted between different units of length.
 */
class LengthUnit {
    companion object {
        const val CM = 2.54
        const val MM = 25.4
        const val M = 0.0254
        const val FT = 1.0 / 12
    }

    // A set of properties which use the inches value to calculate the other units
    var inches: Double = 0.0
        set(value) {
            field = value
            cm = value / CM
            mm = value / MM
            m = value / M
            ft = value / FT
        }

    var cm: Double = 0.0
        set(value) {
            field = value
            inches = value * CM
        }

    var mm: Double = 0.0
        set(value) {
            field = value
            inches = value * MM
        }

    var m: Double = 0.0
        set(value) {
            field = value
            inches = value * M
        }

    var ft: Double = 0.0
        set(value) {
            field = value
            inches = value * FT
        }
}

// A set of extension properties for int and double to convert between units of length
val Int.inches: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.inches = this.toDouble()
        return lengthUnit
    }

val Int.cm: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.cm = this.toDouble()
        return lengthUnit
    }

val Int.mm: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.mm = this.toDouble()
        return lengthUnit
    }

val Int.m: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.m = this.toDouble()
        return lengthUnit
    }

val Int.ft: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.ft = this.toDouble()
        return lengthUnit
    }

val Double.inches: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.inches = this
        return lengthUnit
    }

val Double.cm: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.cm = this
        return lengthUnit
    }

val Double.mm: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.mm = this
        return lengthUnit
    }

val Double.m: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.m = this
        return lengthUnit
    }

val Double.ft: LengthUnit
    get() {
        val lengthUnit = LengthUnit()
        lengthUnit.ft = this
        return lengthUnit
    }