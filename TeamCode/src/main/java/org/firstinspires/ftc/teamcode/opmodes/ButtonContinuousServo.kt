package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.templates.ContinuousAxisScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name = "ButtonContinuousServo")
class ButtonContinuousServo: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, input = {
            ContinuousAxisScript.threeWayServoInput(
                pos = {(gamepad1.dpad_up)}, neg = {(gamepad1.dpad_down)}, stop = {(gamepad1.dpad_right)}, inverted = { false }
            )
        }))
    }

    override fun run() {

    }

    override fun onStop() {

    }
}