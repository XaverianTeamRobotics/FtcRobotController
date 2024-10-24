package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@Disabled
@TeleOp
class ServoMover : BaseOpMode() {
    lateinit var servo: Servo

    override fun construct() {
        servo = HardwareManager.servos[0]
    }

    override fun run() {
        while (true) {
            servo.position = 0.0
            displayStep(1)
            sleep(333)
            servo.position = 0.5
            displayStep(2)
            sleep(333)
            servo.position = 1.0
            displayStep(3)
            sleep(333)
        }
    }

    fun displayStep(step: Int) {
        telemetry.addData("Step", step)
        telemetry.update()
    }

    override fun onStop() {

    }
}