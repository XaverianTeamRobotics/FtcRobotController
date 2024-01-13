package org.firstinspires.ftc.teamcode.internals.math.units

class AngleUnit internal constructor() {
    var deg: Double = 0.0
    var rad: Double
        set(value) {
            deg = value * 180 / Math.PI
        }
        get() = deg * Math.PI / 180
    var grad: Double
        set(value) {
            deg = value * 9 / 10
        }
        get() = deg * 10 / 9
    var turn: Double
        set(value) {
            deg = value * 360
        }
        get() = deg / 360

    operator fun plus(other: AngleUnit): AngleUnit {
        val result = AngleUnit()
        result.deg = this.deg + other.deg
        return result
    }

    operator fun minus(other: AngleUnit): AngleUnit {
        val result = AngleUnit()
        result.deg = this.deg - other.deg
        return result
    }

    operator fun times(other: AngleUnit): AngleUnit {
        val result = AngleUnit()
        result.deg = this.deg * other.deg
        return result
    }

    operator fun div(other: AngleUnit): AngleUnit {
        val result = AngleUnit()
        result.deg = this.deg / other.deg
        return result
    }

    operator fun rem(other: AngleUnit): AngleUnit {
        val result = AngleUnit()
        result.deg = this.deg % other.deg
        return result
    }

    override fun equals(other: Any?): Boolean {
        return if (other is AngleUnit) {
            this.deg == other.deg
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return deg.hashCode()
    }
}

var Double.deg: AngleUnit
    get() = AngleUnit().apply { deg = this@deg }
    set(value) {
        deg.deg = value.deg
    }

var Double.rad: AngleUnit
    get() = AngleUnit().apply { rad = this@rad }
    set(value) {
        deg.rad = value.rad
    }

var Double.grad: AngleUnit
    get() = AngleUnit().apply { grad = this@grad }
    set(value) {
        deg.grad = value.grad
    }

var Double.turn: AngleUnit
    get() = AngleUnit().apply { turn = this@turn }
    set(value) {
        deg.turn = value.turn
    }

var Int.deg: AngleUnit
    get() = AngleUnit().apply { deg = this@deg.toDouble() }
    set(value) {
        deg.deg = value.deg
    }

var Int.rad: AngleUnit
    get() = AngleUnit().apply { deg = this@rad.toDouble() }
    set(value) {
        deg.rad = value.rad
    }

var Int.grad: AngleUnit
    get() = AngleUnit().apply { deg = this@grad.toDouble() }
    set(value) {
        deg.grad = value.grad
    }

var Int.turn: AngleUnit
    get() = AngleUnit().apply { deg = this@turn.toDouble() }
    set(value) {
        deg.turn = value.deg
    }