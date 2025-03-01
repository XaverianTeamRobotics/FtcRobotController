package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.templates.ContinuousAxisScript

/**
 * Script for continuously controlling a motor based on gamepad input.
 *
 * @property id The identifier for the motor.
 * @property inverted Boolean indicating if the motor direction is inverted.
 * @property input Lambda function to get the input value for the motor.
 */
class ContinuousMotorScript(
    private val name: String,
    private val inverted: Boolean = false,
    private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }
) : ContinuousAxisScript(name, inverted, input) {
    constructor(id: Int = 0, inverted: Boolean = false, input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }) : this("cm$id", inverted, input)

    val motor = motors.get(name, 0)
    override val loggingPrefix = "CMS"

    override fun doTheThing(input: Double) {
        motor.power = input
    }

    /**
     * This code was relocated to ContinuousAxisScript, but because we are all friends here (definitely not because im
     * too lazy to change all the references), I will leave this here.
     */
    companion object {
        /**
         * Creates a three-way input function based on positive, negative, and stop conditions.
         *
         * @param pos Lambda function to check the positive condition.
         * @param neg Lambda function to check the negative condition.
         * @param stop Lambda function to check the stop condition.
         * @return Lambda function that returns the input value based on the conditions.
         */
        @Deprecated("Use method in ContinuousAxisScript instead.")
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
        @Deprecated("Use method in ContinuousAxisScript instead.")
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
        @Deprecated("Use method in ContinuousAxisScript instead.")
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