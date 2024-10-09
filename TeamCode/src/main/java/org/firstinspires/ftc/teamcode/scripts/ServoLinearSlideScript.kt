package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.Script

class ServoLinearSlideScript(id: Int = 0, private val inverted: Boolean = false,
                             private val positiveDirection: () -> Float, private val negativeDirection: () -> Float) : Script() {
    private val servo = HardwareManager.servos.get("ls$id", 0)

    override fun init() {

    }

    override fun run() {
        while (true) {
            val input = ((if (inverted) -1.0 else 1.0) * (positiveDirection() - negativeDirection()))
            servo.position = (input/2) + 0.5
        }
    }

    override fun onStop() {

    }
}