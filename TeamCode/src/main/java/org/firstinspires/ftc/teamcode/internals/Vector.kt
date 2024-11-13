package org.firstinspires.ftc.teamcode.internals

import org.firstinspires.ftc.teamcode.internals.units.AngleUnit
import org.firstinspires.ftc.teamcode.internals.units.LengthUnit
import org.firstinspires.ftc.teamcode.internals.units.inches
import org.firstinspires.ftc.teamcode.internals.units.radians
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class Vector(val x: LengthUnit, val y: LengthUnit) {
    val magnitude: LengthUnit
        get() = sqrt(x.inches.pow(2) + y.inches.pow(2)).inches

    val angle: AngleUnit
        get() = atan2(y.inches, x.inches).radians

    constructor(magnitude: LengthUnit, angle: AngleUnit) : this(
        magnitude * angle.cos,
        magnitude * angle.sin
    )

    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y)
    operator fun times(scalar: Double) = Vector(x * scalar, y * scalar)

    fun rotate(angle: AngleUnit) = Vector(
        x * angle.cos - y * angle.sin,
        x * angle.sin + y * angle.cos
    )

    fun normalize() = this * (1 / magnitude.inches)

    override fun toString() = "Vector(x=$x, y=$y)"

    fun intersects(point1: Vector, point2: Vector): Boolean {
        // Use the IVT to determine if we are between the two points
        val minX = point1.x.inches.coerceAtMost(point2.x.inches)
        val maxX = point1.x.inches.coerceAtLeast(point2.x.inches)

        val minY = point1.y.inches.coerceAtMost(point2.y.inches)
        val maxY = point1.y.inches.coerceAtLeast(point2.y.inches)

        return x.inches in minX..maxX && y.inches in minY..maxY
    }
}