package org.firstinspires.ftc.teamcode.opmodes.slides

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook.Companion.findInitialPositionFromAbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript

@TeleOp
class LimitedLinearSlide: BaseOpMode() {
    lateinit var hook: EncoderLimiterHook
    lateinit var motor: DcMotorEx

    override fun construct() {
        motor = HardwareManager.motors[0] as DcMotorEx
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        val absoluteEncoder = HardwareManager.absoluteEncoders[0]
        absoluteEncoder.minVoltage = 0.0
        absoluteEncoder.maxVoltage = 3.3
        val absInchPerRev = 17.752
        val absInitial = 0.0767
        val lowLimit = 0.0
        val highLimit = 14.1
        val encoderTicksPerInch = 516.170
        hook = EncoderLimiterHook(
            //findInitialPositionFromAbsoluteEncoder(absoluteEncoder.position, absInchPerRev, absInitial),
            0.0,
            lowLimit, highLimit, "slide", { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }, encoderTicksPerInch)

        addScript(ContinuousMotorScript(0, false, hook.getHook()))
    }

    override fun run() {
        while (opModeIsActive()) {
            telemetry.addData("Pos", motor.currentPosition)
            telemetry.addData("Velocity", motor.velocity)
            telemetry.update()
        }
    }

    override fun onStop() {

    }
}