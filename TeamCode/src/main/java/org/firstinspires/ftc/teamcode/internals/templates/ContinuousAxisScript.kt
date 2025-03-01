package org.firstinspires.ftc.teamcode.internals.templates

import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad1

/**
 * Script for continuously controlling a motor based on gamepad input.
 *
 * @property id The identifier for the motor.
 * @property inverted Boolean indicating if the motor direction is inverted.
 * @property input Lambda function to get the input value for the motor.
 */
abstract class ContinuousAxisScript(
    private val name: String,
    private val inverted: Boolean = false,
    private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }
) : Script() {
    abstract val loggingPrefix: String

    /**
     * Initializes the script. This method is called once when the script is started.
     */
    override fun init() {}

    /**
     * Main loop for continuously controlling the motor. This method runs continuously.
     */
    override fun run() {
        try {
            var prev: Double = 0.0
            while (scriptIsActive()) {
                val i = (if (inverted) -1.0 else 1.0) * input()
                if (i != prev) {
                    doTheThing(i)
                    RobotLog.v("$name: Set power to $i")
                }
                prev = i
            }
        } catch (e: Exception) {
            RobotLog.e("$name: ${e.message}")
            return
        }
    }

    abstract fun doTheThing(input: Double)

    /**
     * Called when the script is stopped.
     */
    override fun onStop() {}

    companion object {
        /**
         * Creates a three-way input function based on positive, negative, and stop conditions.
         *
         * @param pos Lambda function to check the positive condition.
         * @param neg Lambda function to check the negative condition.
         * @param stop Lambda function to check the stop condition.
         * @return Lambda function that returns the input value based on the conditions.
         */
        fun threeWayInput(pos: () -> Boolean, neg: () -> Boolean, stop: () -> Boolean): () -> Double {
            var state = 0.0
            return {
                if (pos()) {
                    state = 1.0
                } else if (neg()) {
                    state = -1.0
                } else if (stop()) {
                    state = 0.0
                }
                state
            }
        }

        /**
         * Creates a two-button input function based on positive and negative conditions.
         *
         * @param pos Lambda function to check the positive condition.
         * @param neg Lambda function to check the negative condition.
         * @param pow The power value to set when the positive condition is met.
         * @return Lambda function that returns the input value based on the conditions.
         */
        fun twoButtonInput(pos: () -> Boolean, neg: () -> Boolean, pow: Double = 1.0): () -> Double {
            return {
                if (pos()) pow
                else if (neg()) -pow
                else 0.0
            }
        }

        /**
         * Creates a two-way toggle input function based on a toggle condition.
         */
        fun twoWayToggleInput(input: () -> Boolean, power: Double = 1.0, idle: Double = 0.0): () -> Double {
            var state = false
            var held = false
            return {
                if (input() && !held) {
                    state = !state
                    held = true
                } else if (!input()) {
                    held = false
                }

                if (state) power else idle
            }
        }
    }
}