package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.Script

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
) : Script() {
    private val servo = HardwareManager.servos.get("cs$id", 0)

    /**
     * Initializes the script. This method is called once when the script is started.
     */
    override fun init() {}

    /**
     * Main loop for continuously controlling the servo. This method runs continuously.
     */
    override fun run() {
        var prev = 0.0
        while (true) {
            val i = ((((if (inverted) -1 else 1) * input()) / 2) + 0.5)
            if (i != prev) {
                servo.position = i
                RobotLog.v("CSS${id}: Set power to ${i}")
            }
            prev = i
        }
    }

    /**
     * Called when the script is stopped.
     */
    override fun onStop() {}
}