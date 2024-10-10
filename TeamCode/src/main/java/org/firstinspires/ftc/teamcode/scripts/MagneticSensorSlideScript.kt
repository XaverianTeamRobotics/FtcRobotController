package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.base.Script
import kotlin.system.exitProcess

/**
 * Script for controlling a slide motor with a magnetic sensor.
 *
 * @property id The identifier for the slide motor and sensor.
 * @property inverted Boolean indicating if the motor direction is inverted.
 * @property input Lambda function to get the input value for the motor.
 */
class MagneticSensorSlideScript(
    id: Int = 0,
    private val inverted: Boolean = false,
    private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }
) : Script() {
    private val slideMotor = HardwareManager.motors.get("ls${id}", 0)
    private val magSensor = HardwareManager.touchSwitches.get("ls${id}", 0)

    /**
     * Initializes the script. This method is called once when the script is started.
     */
    override fun init() {}

    /**
     * Main loop for controlling the slide motor. This method runs continuously.
     * It adjusts the motor power based on the input and the state of the magnetic sensor.
     */
    override fun run() {
        while (true) {
            val modifier = if (inverted) -1.0 else 1.0
            var i = input()
            if (magSensor.isPressed) {
                i = i.coerceIn(-1.0, 0.0)
            }
            slideMotor.power = i * modifier
        }
    }

    /**
     * Called when the script is stopped. Currently, this method does nothing.
     */
    override fun onStop() {}
}