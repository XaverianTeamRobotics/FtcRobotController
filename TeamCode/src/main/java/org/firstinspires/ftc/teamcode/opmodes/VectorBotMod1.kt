package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript


@TeleOp(name = "VectorBot Model 1", group = BaseOpMode.FULL_GROUP_NAME)
class VectorBotMod1: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.8)) //driver 1 driving the robot
        addScript(ContinuousMotorScript(id = 5, input = {(gamepad2.right_stick_y).toDouble()})) //vertical slide
        addScript(ContinuousMotorScript(id = 6, inverted = true, input = {(gamepad2.right_stick_y).toDouble()})) //other vertical lifter
        addScript(ContinuousMotorScript(id = 7, input = {(gamepad2.right_stick_x).toDouble()})) //lifter adjuster
        addScript(ContinuousServoScript(id = 0, input = {(gamepad2.left_stick_y).toDouble()})) //horizontal slide 1
        addScript(ContinuousServoScript(id = 1, input = {(gamepad2.left_stick_y).toDouble()})) //horizontal slide 2
        addScript(ContinuousServoScript(id = 2, input = ContinuousMotorScript.threeWayInput({gamepad2.dpad_down}, {gamepad2.dpad_up}, {gamepad2.dpad_right}))) //intake
        addScript(ContinuousServoScript(id = 3, input = ContinuousMotorScript.threeWayInput({gamepad2.dpad_up}, {gamepad2.dpad_down}, {gamepad2.dpad_right}))) //other intake
        addScript(ContinuousServoScript(id = 4, input = {(gamepad2.right_trigger-gamepad2.left_trigger).toDouble()})) //claw rotator
        addScript(ContinuousServoScript(id = 5, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.x}))) //claw grabber
        addScript(ContinuousServoScript(id = 6, input = {(gamepad1.right_trigger-gamepad1.left_trigger).toDouble()})) //screw jack
    }

    override fun run() {
    }

    override fun onStop() {
    }
}