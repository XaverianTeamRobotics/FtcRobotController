package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook
import org.firstinspires.ftc.teamcode.hooks.EncoderLimiterHook.Companion.findInitialPositionFromAbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.templates.ContinuousAxisScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript

@TeleOp
class Bigbot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = false, inverted = true))
        addScript(ContinuousMotorScript("hslide", input = {hSlideInput()}))
        addScript(ContinuousMotorScript("vslide", input = {gamepad2.right_stick_y.toDouble()}))

        addScript(ContinuousMotorScript("lift1", input = ContinuousAxisScript.twoButtonInput({gamepad1.dpad_right}, {gamepad1.dpad_left})))
        addScript(ContinuousMotorScript("lift2", input = ContinuousAxisScript.twoButtonInput({gamepad1.dpad_up}, {gamepad1.dpad_down})))

        addScript(ContinuousServoScript("uptilt", input = {gamepad2.left_stick_x.toDouble()}))

        addScript(ContinuousServoScript("intake", input = ContinuousAxisScript.twoButtonInput({gamepad2.dpad_up}, {gamepad2.dpad_down})))
    }

    override fun run() {

    }

    override fun onStop() {

    }

    fun hSlideInput(): Double {
        val x = gamepad2.left_stick_y.toDouble()
        return if (x in -0.10..0.05) {
            -0.2
        } else {
            x
        }
    }
}