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
import org.firstinspires.ftc.teamcode.scripts.TiltAndExtendScript

@TeleOp
class Bigbot: BaseOpMode() {
    override fun construct() {

    }

    override fun run() {
        addScript(MecanumDriveScript(hasBevelGears = false, inverted = true))
        addScript(TiltAndExtendScript(tiltMaxVel = 40.0, extendMaxVel = 1000.0, extend2MaxVel = 1000.0, pMultiplier = 1.5, gamepadEnable = true))
        sleep(500)
        addScript(ContinuousServoScript("intake", input = {((gamepad2.right_stick_y.toDouble() / 4) + 0.5)}))
        addScript(ContinuousServoScript("tilt", input = {(gamepad2.left_stick_y.toDouble() / 2) + 0.5}))
    }

    override fun onStop() {

    }
}