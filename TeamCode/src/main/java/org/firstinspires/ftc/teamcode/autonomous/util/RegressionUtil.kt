package org.firstinspires.ftc.teamcode.autonomous.util

import com.acmerobotics.roadrunner.kinematics.Kinematics.calculateMotorFeedforward
import org.apache.commons.math3.stat.regression.SimpleRegression
import org.firstinspires.ftc.teamcode.autonomous.util.RegressionUtil.AccelResult
import org.firstinspires.ftc.teamcode.autonomous.util.RegressionUtil.RampResult
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.ArrayList
import kotlin.math.abs

/**
 * Various regression utilities.
 */
object RegressionUtil {
    /**
     * Numerically compute dy/dx from the given x and y values. The returned list is padded to match
     * the length of the original sequences.
     *
     * @param x x-values
     * @param y y-values
     * @return derivative values
     */
    private fun numericalDerivative(x: List<Double>, y: List<Double>): List<Double> {
        val deriv = ArrayList<Double>(x.size)
        for (i in 1 until x.size - 1) {
            deriv.add(
                (y[i + 1] - y[i - 1]) /
                        (x[i + 1] - x[i - 1])
            )
        }
        // copy endpoints to pad output
        deriv.add(0, deriv.get(0))
        deriv.add(deriv.get(deriv.size - 1))
        return deriv.toList()
    }

    /**
     * Run regression to compute velocity and static feedforward from ramp test data.
     *
     * Here's the general procedure for gathering the requisite data:
     * 1. Slowly ramp the motor power/voltage and record encoder values along the way.
     * 2. Run a linear regression on the encoder velocity vs. motor power plot to obtain a slope
     * (kV) and an optional intercept (kStatic).
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param fitStatic fit kStatic
     * @param file log file
     */
    fun fitRampData(
        timeSamples: List<Double>, positionSamples: List<Double>,
        powerSamples: List<Double>, fitStatic: Boolean,
        file: File?
    ): RampResult {
        if (file != null) {
            try {
                PrintWriter(file).use { pw ->
                    pw.println("time,position,power")
                    for (i in timeSamples.indices) {
                        val time: Double = timeSamples.get(i)
                        val pos: Double = positionSamples.get(i)
                        val power: Double = powerSamples.get(i)
                        pw.println(time.toString() + "," + pos + "," + power)
                    }
                }
            } catch (e: FileNotFoundException) {
                // ignore
            }
        }

        val velSamples = numericalDerivative(timeSamples, positionSamples)

        val rampReg = SimpleRegression(fitStatic)
        for (i in timeSamples.indices) {
            val vel: Double = velSamples.get(i)
            val power: Double = powerSamples.get(i)

            rampReg.addData(vel, power)
        }

        return RampResult(
            abs(rampReg.getSlope()), abs(rampReg.getIntercept()),
            rampReg.getRSquare()
        )
    }

    /**
     * Run regression to compute acceleration feedforward.
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param rampResult ramp result
     * @param file log file
     */
    fun fitAccelData(
        timeSamples: List<Double>, positionSamples: List<Double>,
        powerSamples: List<Double>, rampResult: RampResult,
        file: File?
    ): AccelResult {
        if (file != null) {
            try {
                PrintWriter(file).use { pw ->
                    pw.println("time,position,power")
                    for (i in timeSamples.indices) {
                        val time: Double = timeSamples.get(i)
                        val pos: Double = positionSamples.get(i)
                        val power: Double = powerSamples.get(i)
                        pw.println(time.toString() + "," + pos + "," + power)
                    }
                }
            } catch (e: FileNotFoundException) {
                // ignore
            }
        }

        val velSamples = numericalDerivative(timeSamples, positionSamples)
        val accelSamples = numericalDerivative(timeSamples, velSamples)

        val accelReg = SimpleRegression(false)
        for (i in timeSamples.indices) {
            val vel: Double = velSamples.get(i)
            val accel: Double = accelSamples.get(i)
            val power: Double = powerSamples.get(i)

            val powerFromVel = calculateMotorFeedforward(
                vel, 0.0, rampResult.kV, 0.0, rampResult.kStatic
            )
            val powerFromAccel = power - powerFromVel

            accelReg.addData(accel, powerFromAccel)
        }

        return AccelResult(abs(accelReg.getSlope()), accelReg.getRSquare())
    }

    /**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    class RampResult(kV: Double, kStatic: Double, rSquare: Double) {
        val kV: Double
        val kStatic: Double
        val rSquare: Double

        init {
            this.kV = kV
            this.kStatic = kStatic
            this.rSquare = rSquare
        }
    }

    /**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    data class AccelResult(val kA: Double, val rSquare: Double)
}
