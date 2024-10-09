package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script

class LinearSlideScript(id: Int = 0, private val inverted: Boolean = false,
                        private val positiveDirection: () -> Float, private val negativeDirection: () -> Float) : Script() {
    private val motor = HardwareManager.motors.get("ls$id", 0)

    override fun init() {

    }

    override fun run() {
        while (true) {
            motor.power = (if (inverted) -1.0 else 1.0) * (positiveDirection() - negativeDirection())
        }
    }

    override fun onStop() {

    }
}