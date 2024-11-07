package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript


@TeleOp(name = "VectorBot Model 1", group = BaseOpMode.FULL_GROUP_NAME)
class VectorBotMod1: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.8))
        addScript(ContinuousMotorScript(id = 4, input = { (gamepad1.right_trigger-gamepad1.left_trigger).toDouble() }))
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.twoWayToggleInput({ gamepad1.x }, power = 0.25)))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}