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
class BigbotHSlide: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousMotorScript("hslide", input = {gamepad2.left_stick_y.toDouble()}))
    }

    override fun run() {

    }

    override fun onStop() {

    }
}