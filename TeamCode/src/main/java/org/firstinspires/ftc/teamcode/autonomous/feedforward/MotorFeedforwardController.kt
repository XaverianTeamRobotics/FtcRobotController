package org.firstinspires.ftc.teamcode.autonomous.feedforward

/**
 * A feedforward controller designed for motors that calculates the output based on the target velocity and acceleration,
 * not considering gravity
 *
 * @param vStatic the static voltage
 * @param kv the velocity coefficient
 * @param ka the acceleration coefficient (default 0)
 */
class MotorFeedforwardController(var vStatic: Double, var kv: Double, var ka: Double = 0.0): FeedforwardController {
    override fun getCoefficients(): List<Double> {
        return listOf(vStatic, kv)
    }

    override fun setCoefficients(input: List<Double>) {
        if (input.size == 2 || input.size == 3) {
            vStatic = input[0]
            kv = input[1]
            if (input.size == 3) ka = input[2]
        } else {
            throw IllegalArgumentException("${input.size} arguments given, 2 or 3 required")
        }
    }

    override fun calculate(targetVelocity: Double, targetAcceleration: Double) =
        vStatic + (kv * targetVelocity) + (ka * targetAcceleration)
}