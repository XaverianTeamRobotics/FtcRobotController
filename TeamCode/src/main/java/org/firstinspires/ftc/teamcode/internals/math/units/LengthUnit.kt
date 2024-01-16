package org.firstinspires.ftc.teamcode.internals.math.units

class LengthUnit internal constructor() {
    var cm: Double = 0.0
    var m: Double
        set(value) {
            cm = value * 100
        }
        get() = cm / 100
    var km: Double
        set(value) {
            m = value * 1000
        }
        get() = m / 1000
    var inch: Double
        set(value) {
            cm = value * 2.54
        }
        get() = cm / 2.54
    var ft: Double
        set(value) {
            inch = value * 12
        }
        get() = inch / 12
    var yd: Double
        set(value) {
            ft = value * 3
        }
        get() = ft / 3
    var mi: Double
        set(value) {
            yd = value * 1760
        }
        get() = yd / 1760

    operator fun plus(other: LengthUnit): LengthUnit {
        val result = LengthUnit()
        result.cm = this.cm + other.cm
        return result
    }

    operator fun minus(other: LengthUnit): LengthUnit {
        val result = LengthUnit()
        result.cm = this.cm - other.cm
        return result
    }

    operator fun times(other: LengthUnit): LengthUnit {
        val result = LengthUnit()
        result.cm = this.cm * other.cm
        return result
    }

    operator fun div(other: LengthUnit): LengthUnit {
        val result = LengthUnit()
        result.cm = this.cm / other.cm
        return result
    }

    operator fun rem(other: LengthUnit): LengthUnit {
        val result = LengthUnit()
        result.cm = this.cm % other.cm
        return result
    }

    override fun equals(other: Any?): Boolean {
        return if (other is LengthUnit) {
            this.cm == other.cm
        } else {
            false
        }
    }

    operator fun compareTo(other: LengthUnit): Int {
        return this.cm.compareTo(other.cm)
    }

    override fun hashCode(): Int {
        return cm.hashCode()
    }
}

var Double.cm: LengthUnit
    get() = LengthUnit().apply { cm = this@cm }
    set(value) {
        cm.cm = value.cm
    }

var Double.m: LengthUnit
    get() = LengthUnit().apply { m = this@m }
    set(value) {
        cm.m = value.m
    }

var Double.km: LengthUnit
    get() = LengthUnit().apply { km = this@km }
    set(value) {
        cm.km = value.km
    }

var Double.inch: LengthUnit
    get() = LengthUnit().apply { inch = this@inch }
    set(value) {
        cm.inch = value.inch
    }

var Double.ft: LengthUnit
    get() = LengthUnit().apply { ft = this@ft }
    set(value) {
        cm.ft = value.ft
    }

var Double.yd: LengthUnit
    get() = LengthUnit().apply { yd = this@yd }
    set(value) {
        cm.yd = value.yd
    }

var Double.mi: LengthUnit
    get() = LengthUnit().apply { mi = this@mi }
    set(value) {
        cm.mi = value.mi
    }

var Int.cm: LengthUnit
    get() = LengthUnit().apply { cm = this@cm.toDouble() }
    set(value) {
        cm.cm = value.cm
    }

var Int.m: LengthUnit
    get() = LengthUnit().apply { m = this@m.toDouble() }
    set(value) {
        cm.m = value.m
    }

var Int.km: LengthUnit
    get() = LengthUnit().apply { km = this@km.toDouble() }
    set(value) {
        cm.km = value.km
    }

var Int.inch: LengthUnit
    get() = LengthUnit().apply { inch = this@inch.toDouble() }
    set(value) {
        cm.inch = value.inch
    }

var Int.ft: LengthUnit
    get() = LengthUnit().apply { ft = this@ft.toDouble() }
    set(value) {
        cm.ft = value.ft
    }

var Int.yd: LengthUnit
    get() = LengthUnit().apply { ft = this@yd.toDouble() }
    set(value) {
        cm.yd = value.yd
    }

var Int.mi: LengthUnit
    get() = LengthUnit().apply { mi = this@mi.toDouble() }
    set(value) {
        cm.mi = value.mi
    }