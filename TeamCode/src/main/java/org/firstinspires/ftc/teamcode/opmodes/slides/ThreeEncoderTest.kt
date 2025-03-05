package org.firstinspires.ftc.teamcode.opmodes.slides

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
class ThreeEncoderTest: BaseOpMode() {
    lateinit var motor0: DcMotorEx
    lateinit var motor1: DcMotorEx
    lateinit var motor2: DcMotorEx

    override fun construct() {
        motor0 = HardwareManager.motors[0] as DcMotorEx
        motor1 = HardwareManager.motors[1] as DcMotorEx
        motor2 = HardwareManager.motors[2] as DcMotorEx
        resetEncoder()
    }

    override fun run() {
        while (opModeIsActive()) {
            if (gamepad1.a) {
                resetEncoder()
            }
            displayFrame.frozen = true
            displayFrame.clear()

            displayFrame.addLine("Position0: ${motor0.currentPosition}")
            displayFrame.addLine("Velocity0: ${motor0.velocity}")

            displayFrame.addLine("Position1: ${motor1.currentPosition}")
            displayFrame.addLine("Velocity1: ${motor1.velocity}")

            displayFrame.addLine("Position2: ${motor2.currentPosition}")
            displayFrame.addLine("Velocity2: ${motor2.velocity}")

            displayFrame.frozen = false
        }
    }

    override fun onStop() {

    }

    fun resetEncoder() {
        motor0.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor0.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        motor1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        motor2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}