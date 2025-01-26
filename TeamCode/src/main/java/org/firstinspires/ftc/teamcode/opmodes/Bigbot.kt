package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook.Companion.findInitialPositionFromAbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript

@TeleOp
class Bigbot: BaseOpMode() {
    lateinit var hSlideHook: EncoderLimiterHook

    override fun construct() {
        HardwareManager.motors[0].mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        HardwareManager.motors[0].mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        hSlideHook = get_HSlideHook()

        addScript(MecanumDriveScript(hasBevelGears = false))
        addScript(ContinuousMotorScript(0, false, hSlideHook.getHook())) // H Slide
        addScript(ContinuousMotorScript(1, false, { HardwareManager.gamepad2.right_stick_y.toDouble() })) // V Slide
    }

    override fun run() {

    }

    override fun onStop() {

    }

    private fun get_HSlideHook(): EncoderLimiterHook {
        val absoluteEncoder = HardwareManager.absoluteEncoders[0]
        absoluteEncoder.minVoltage = 0.0
        absoluteEncoder.maxVoltage = 3.3
        val absInchPerRev = 17.752
        val absInitial = 0.0767
        val lowLimit = 0.0
        val highLimit = 14.1
        val encoderTicksPerInch = 516.170
        return EncoderLimiterHook(
            //findInitialPositionFromAbsoluteEncoder(absoluteEncoder.position, absInchPerRev, absInitial),
            0.0,
            lowLimit, highLimit, "cm0", { (gamepad2.left_stick_y).toDouble() }, encoderTicksPerInch)
    }
}