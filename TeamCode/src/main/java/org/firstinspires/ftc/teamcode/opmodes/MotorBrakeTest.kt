package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.toEx
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
class MotorBrakeTest: BaseOpMode() {
    lateinit var motor: DcMotorEx
    override fun construct() {
        motor = motors[0].toEx()
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.velocity = 1000.0
        motor.targetPosition = 0
        motor.power = 1.0
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.targetPositionTolerance = 1
        val k = motor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION)
        k.p *= 2
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, k)
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    override fun run() {
        while (opModeIsActive()) {
            val k = motor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION)
            telemetry.addLine("P - ${k.p}, I - ${k.i}, D - ${k.d}, F - ${k.f}")
            telemetry.addData("Current", motor.getCurrent(CurrentUnit.MILLIAMPS))
            telemetry.addData("Position", motor.currentPosition)
            telemetry.update()
        }
    }

    override fun onStop() {

    }

}