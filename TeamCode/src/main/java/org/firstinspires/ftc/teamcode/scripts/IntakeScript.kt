package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad2
import org.firstinspires.ftc.teamcode.internals.templates.Script

class IntakeScript: Script() {
    private val intake = HardwareManager.servos.get("intake", 5)

    override fun init() {
        intake.position = 0.5
    }

    override fun run() {
        if (gamepad2.dpad_up) intake.position = 1.0
        else if (gamepad2.dpad_down) intake.position = 0.0
        else if (gamepad2.dpad_left || gamepad2.dpad_right) {
            intake.position = 0.3
            Thread.sleep(50)
            intake.position = 0.5
        }
    }

    override fun onStop() {

    }
}