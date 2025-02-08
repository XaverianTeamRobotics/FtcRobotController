package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.templates.ContinuousAxisScript

/**
 * Script for continuously controlling a servo based on gamepad input.
 *
 * @property id The identifier for the servo.
 * @property inverted Boolean indicating if the servo direction is inverted.
 * @property input Lambda function to get the input value for the servo.
 */
class ContinuousServoScript(
    private val id: Int = 0,
    private val inverted: Boolean = false,
    private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }
) : ContinuousAxisScript(id, inverted, input) {
    private val servo = HardwareManager.servos.get("cs$id", 0)
    override val loggingPrefix: String = "CSS"

    override fun doTheThing(input: Double) {
        servo.position = input
    }
}