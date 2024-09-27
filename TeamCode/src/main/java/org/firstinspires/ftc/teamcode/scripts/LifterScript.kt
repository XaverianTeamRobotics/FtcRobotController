package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script

class LifterScript(id: Int = 0, private val inverted: Boolean = false,
                   private val input: () -> Boolean = {(gamepad1.right_bumper)}): Script() {
    private val motor = motors.get("ls$id",4)
    override fun init() {
    }

    override fun run() {
        while(true){
            if (gamepad1.right_bumper){
                motor.power = 1.0
            }
            else if (gamepad1.left_bumper){
                motor.power = -1.0
            }
            else{
                motor.power = 0.0
            }
        }
    }

    override fun onStop() {
    }
}