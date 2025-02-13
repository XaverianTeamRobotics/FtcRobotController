package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name = "Top-Down Intake", group = BaseOpMode.DEBUG_GROUP_NAME)
class TopDownIntake: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.threeWayInput(
            pos = {(gamepad1.dpad_up)}, neg = {(gamepad1.dpad_down)}, stop = {(gamepad1.dpad_right)}
        )))
        addScript(ContinuousServoScript(id = 1, input = ContinuousMotorScript.threeWayInput(
            pos = {(gamepad1.dpad_up)}, neg = {(gamepad1.dpad_down)}, stop = {(gamepad1.dpad_right)}
        )))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}