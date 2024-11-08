package org.firstinspires.ftc.teamcode.autonomous.feedforward

/**
 * A feedforward controller that calculates the output based on the target velocity and acceleration
 */
interface FeedforwardController {
    /**
     * Get the coefficients of the feedforward controller
     */
    fun getCoefficients(): List<Double>

    /**
     * Set the coefficients of the feedforward controller
     * @throws IllegalArgumentException if the number of coefficients is incorrect
     */
    fun setCoefficients(input: List<Double>)

    /**
     * Calculate the output based on the target velocity and acceleration
     */
    fun calculate(targetVelocity: Double, targetAcceleration: Double = 0.0): Double
}