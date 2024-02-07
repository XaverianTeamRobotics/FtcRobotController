package org.firstinspires.ftc.teamcode.internals.misc

import com.qualcomm.robotcore.util.Range
import kotlin.math.exp
import kotlin.math.ln

/**
 * Calculates a limit of a [slew rate](https://en.wikipedia.org/wiki/Slew_rate) such that <kbd>limit = ln(weight)</kbd> where the logarithm is within a certain area. Check this [visualization](https://www.desmos.com/calculator/rziziaen6n) for more details.
 */
class RatelimitCalc(private val y1: Double, private val y2: Double, private val min: Double, private val max: Double) {
    private val x1 = 1.0
    private val x2 = 101.0

    fun calculate(position: Int): Double {
        val pos = normalizePosition(position)
        return internalCalc(pos)
    }

    private fun normalizePosition(p: Int): Double {
        var p = p
        p = Range.clip(p, min.toInt(), max.toInt())
        return Range.scale(p.toDouble(), min, max, 1.0, 101.0)
    }

    private fun internalCalc(p: Double): Double {
        return f(p)
    }

    private fun a(): Double {
        //       numerator of m
        // a = ------------------
        //      ln of denom of m
        return (y1 - y2) / ln(x1 / x2)
    }

    private fun b(): Double {
        //      y2 * ln of x1 - y1 * ln of x2
        // b = -------------------------------
        //                 y1 - y2
        return exp((y2 * ln(x1) - y1 * ln(x2)) / (y1 - y2))
    }

    private fun f(x: Double): Double {
        //
        // f = a * ln of (b * x) as long as its on [x1, x2], otherwise its y1 or y2
        //
        return if (x < x1) {
            y1
        } else if (x <= x2) {
            a() * ln(b() * x)
        } else {
            y2
        }
    }
}
