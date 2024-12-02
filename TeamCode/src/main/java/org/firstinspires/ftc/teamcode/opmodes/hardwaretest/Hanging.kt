package org.firstinspires.ftc.teamcode.opmodes.hardwaretest

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.scripts.hardware.ContinuousMotorScript

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