package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.templates.Script
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad1

class HangingScript(): Script() {
    private val liftMotor: DcMotor = motors.get("liftMotor", 4)
    private val adjustLifter: DcMotor = motors.get("adjustLifter", 5)
    override fun init() {
    }

    override fun run() {
        while(true){
            liftMotor.power = gamepad1.right_stick_y.toDouble()
            adjustLifter.power = gamepad1.left_stick_y.toDouble()
        }
    }

    override fun onStop() {
    }
}