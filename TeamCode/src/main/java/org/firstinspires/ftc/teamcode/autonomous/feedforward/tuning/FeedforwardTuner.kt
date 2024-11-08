package org.firstinspires.ftc.teamcode.autonomous.feedforward.tuning

/**
 * A feedforward tuner that generates coefficients for a feedforward controller
 */
interface FeedforwardTuner {
    /**
     * Generate coefficients for a feedforward controller based on the given data
     * @param data a map of power vs velocity data points
     * @return a list of coefficients
     */
    fun generateCoefficients(data: Map<Double, Double>): List<Double>
}