package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript


@TeleOp(name = "Servo Mode Servo", group = BaseOpMode.DEBUG_GROUP_NAME)
class TwoWayToggleServo: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.twoWayToggleInput({ gamepad1.x }, power = 1.0, idle = -1.0)))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}