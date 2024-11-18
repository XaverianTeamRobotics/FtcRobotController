package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript


@TeleOp(name = "Intake", group = BaseOpMode.DEBUG_GROUP_NAME)
class Intake: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, inverted = false, input = {((gamepad1.right_trigger - gamepad1.left_trigger).toDouble())}))
        addScript(ContinuousServoScript(id = 1, inverted = true, input = {(gamepad1.right_trigger - gamepad1.left_trigger).toDouble()}))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}