package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script

class LinearSlideScript(id: Int = 0, private val inverted: Boolean = false,
                        private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }) : Script() {
    private val motor = motors.get("ls$id", 0)

    override fun init() {

    }

    override fun run() {
        while (true) {
            motor.power = (if (inverted) -1.0 else 1.0) * input()
        }
    }

    override fun onStop() {

    }
}