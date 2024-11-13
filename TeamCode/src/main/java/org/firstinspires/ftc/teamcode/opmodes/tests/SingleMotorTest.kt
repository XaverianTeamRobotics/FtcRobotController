package org.firstinspires.ftc.teamcode.opmodes.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript

@TeleOp(name = "Single Motor Test", group = BaseOpMode.DEBUG_GROUP_NAME)
class SingleMotorTest: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousMotorScript(id = 0, input = {
            HardwareManager.gamepad1.right_trigger.toDouble()-HardwareManager.gamepad1.left_trigger.toDouble() }))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}