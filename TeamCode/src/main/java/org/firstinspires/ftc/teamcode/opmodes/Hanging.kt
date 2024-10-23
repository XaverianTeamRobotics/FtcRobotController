package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript

@TeleOp(name = "Hanging", group = BaseOpMode.DEBUG_GROUP_NAME)
class Hanging: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousMotorScript(id = 4, input = { HardwareManager.gamepad1.right_stick_y.toDouble() })) // Lifter
        addScript(ContinuousMotorScript(id = 5, input = { HardwareManager.gamepad1.left_stick_y.toDouble() })) // Adjuster
    }

    override fun run() {
    }

    override fun onStop() {
    }
}