package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name = "ContinuousServo", group = BaseOpMode.DEBUG_GROUP_NAME)
class ContinuousServo: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, input = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }))
        addScript(ContinuousServoScript(id = 1, input = { (gamepad1.left_trigger - gamepad1.right_trigger).toDouble() }))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}