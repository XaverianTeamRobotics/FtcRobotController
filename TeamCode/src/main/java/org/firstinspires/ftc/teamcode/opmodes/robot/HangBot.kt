package org.firstinspires.ftc.teamcode.opmodes.robot

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.hardware.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.drive.MecanumDriveScript


@TeleOp(name = "HangBot", group = BaseOpMode.FULL_GROUP_NAME)
class HangBot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.8))
        addScript(ContinuousMotorScript(id = 5, input = {(gamepad2.left_stick_y).toDouble()}))
        addScript(ContinuousMotorScript(id = 4, input = {(gamepad2.right_trigger-gamepad2.left_trigger).toDouble()}))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}