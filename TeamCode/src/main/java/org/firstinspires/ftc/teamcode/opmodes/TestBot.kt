package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript

@TeleOp(name = "TestBot", group = BaseOpMode.FULL_GROUP_NAME)
class TestBot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(rotScale = 0.6)) //mecanum drive
        addScript(ContinuousMotorScript(id = 5, input = {(gamepad2.right_trigger-gamepad2.left_trigger).toDouble()})) //motor for linear slides
        addScript(ContinuousMotorScript(id = 4, input = {(gamepad1.right_trigger-gamepad1.left_trigger).toDouble()})) //motor for arm rotator thingy
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.threeWayInput(
            pos = {(gamepad2.dpad_up)}, neg = {(gamepad2.dpad_down)}, stop = {(gamepad2.dpad_right)}
        ))) //first intake servo - MAKE SURE THIS SERVO IS IN CONTINUOUS MODE
        addScript(ContinuousServoScript(id = 1, inverted = true, input = ContinuousMotorScript.threeWayInput(
            pos = {(gamepad2.dpad_up)}, neg = {(gamepad2.dpad_down)}, stop = {(gamepad2.dpad_right)}
        ))) //second intake servo - MAKE SURE THIS SERVO IS IN CONTINUOUS MODE
    }

    override fun run() {

    }

    override fun onStop() {

    }
}