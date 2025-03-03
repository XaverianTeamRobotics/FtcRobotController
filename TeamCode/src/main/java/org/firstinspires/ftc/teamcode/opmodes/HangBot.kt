package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript


@TeleOp(name = "HangBot", group = BaseOpMode.FULL_GROUP_NAME)
class HangBot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.8))
        addScript(ContinuousMotorScript(id = 5, input = {(gamepad2.right_trigger-gamepad2.left_trigger).toDouble()}))
        addScript(ContinuousMotorScript(id = 4, input = {(gamepad1.right_trigger-gamepad1.left_trigger).toDouble()}))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}