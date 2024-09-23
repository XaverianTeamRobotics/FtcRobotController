package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.base.Script
import kotlin.system.exitProcess

class MagneticSensorSlideScript(id: Int=0, private val inverted: Boolean = false): Script() {
    private val slideMotor = HardwareManager.motors.get("ls${id}", 0);
    private val magSensor = HardwareManager.touchSwitches.get("ls${id}", 0)

    override fun init() {

    }

    override fun run() {
        while (true) {
            val modifier = if (inverted) -1.0 else 1.0
            var input = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()
            if (magSensor.isPressed) {
                input = input.coerceIn(-1.0, 0.0);
            }
            slideMotor.power = input * modifier
        }
    }

    override fun onStop() {

    }
}