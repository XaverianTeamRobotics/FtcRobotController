package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.base.Script
import kotlin.system.exitProcess

class MagneticSensorSlideScript(id: Int=0, private val inverted: Boolean = false,
                                private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }): Script() {
    private val slideMotor = HardwareManager.motors.get("ls${id}", 0);
    private val magSensor = HardwareManager.touchSwitches.get("ls${id}", 0)

    override fun init() {

    }

    override fun run() {
        while (true) {
            val modifier = if (inverted) -1.0 else 1.0
            var i = input()
            if (magSensor.isPressed) {
                i = i.coerceIn(-1.0, 0.0);
            }
            slideMotor.power = i * modifier
        }
    }

    override fun onStop() {

    }
}