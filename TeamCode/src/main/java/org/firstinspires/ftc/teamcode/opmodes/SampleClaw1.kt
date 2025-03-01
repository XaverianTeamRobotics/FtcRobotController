package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name = "SampleClaw1", group = BaseOpMode.DEBUG_GROUP_NAME)
class SampleClaw1: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.twoWayToggleInput(
            input = { gamepad1.dpad_up }, power = 1.0, idle = -1.0)))
        addScript(ContinuousServoScript(id = 1, input = ContinuousMotorScript.twoWayToggleInput(
            input = { gamepad1.dpad_right }, power = 1.0, idle = -1.0)))
        addScript(ContinuousServoScript(id = 2, input = ContinuousMotorScript.twoWayToggleInput(
            input = { gamepad1.x }, power = 1.0, idle = -1.0)))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}