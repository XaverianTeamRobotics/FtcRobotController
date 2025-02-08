package org.firstinspires.ftc.teamcode.opmodes.slides

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.AbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
class EncoderTest: BaseOpMode() {
    lateinit var motor: DcMotorEx
    override fun construct() {
        resetEncoder()
        motor = HardwareManager.motors[0] as DcMotorEx
    }

    override fun run() {
        while (opModeIsActive()) {
            if (gamepad1.a) {
                resetEncoder()
            }
            telemetry.addData("Position", motor.currentPosition)
            telemetry.addData("Velocity", motor.velocity)
            telemetry.update()
        }
    }

    override fun onStop() {

    }

    fun resetEncoder() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}