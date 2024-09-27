package org.firstinspires.ftc.teamcode.scripts


import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.Script

class IntakeScript(id: Int=0, private val inverted: Boolean=false,
    private val input: () -> Boolean = {(gamepad1.y)}): Script() {

    private val servo = HardwareManager.servos.get("ls$id", 1)
    override fun init() {
    }

    override fun run() {
        while (true){
            if (gamepad1.y){
                servo.position = 1.0 //intake
            }
            if (gamepad1.a){
                servo.position = -1.0 //outtake
            }
            if (gamepad1.b){
                servo.position = 0.0 //stop
            }
        }
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }
}