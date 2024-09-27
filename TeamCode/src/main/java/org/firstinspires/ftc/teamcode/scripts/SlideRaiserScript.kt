package org.firstinspires.ftc.teamcode.scripts


import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.Script

class SlideRaiserScript(id: Int = 0, private val inverted: Boolean=false,
                        private val input: () -> Boolean = {(gamepad1.dpad_up)}): Script() {
    private val motor = HardwareManager.motors.get("ls$id",5)
    override fun init() {
    }

    override fun run() {
        while (true){
            if (gamepad1.dpad_up){
                motor.power = 1.0
            }
            else if (gamepad1.dpad_down){
                motor.power = -1.0
            }
            else {
                motor.power = 0.0
            }
        }
    }

    override fun onStop() {
    }
}