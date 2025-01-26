package org.firstinspires.ftc.teamcode.hooks

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager

/**
 * @param initialPosition The initial position, in inches
 * @param lowLimit The lower bound, in inches
 * @param highLimit The upper bound, in inches
 */
class EncoderLimiterHook(val initialPosition: Double, @Volatile var lowLimit: Double, @Volatile var highLimit: Double, encoderName: String, val input: () -> Double, private val encoderTicksPerInch: Double) {
    val encoder = HardwareManager.motors[encoderName]
    private val encInitialPosition = encoder.currentPosition

    fun getEncoderPos() = ((encoder.currentPosition.toDouble() - encInitialPosition) / encoderTicksPerInch) + initialPosition

    fun getHook(): () -> Double = {
        val x = getEncoderPos()
        val i = input()
        if (x >= highLimit) i.coerceIn(-1.0, 0.0)
        else if (x <= lowLimit) i.coerceIn(0.0, 1.0)
        else i
    }

    companion object {
        fun findInitialPositionFromAbsoluteEncoder(absPosition: Double, inchPerRev: Double, absInitial: Double): Double {
            return (absPosition-absInitial) * inchPerRev
        }
    }
}