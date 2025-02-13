package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript


@TeleOp(name = "VectorBot", group = BaseOpMode.FULL_GROUP_NAME)
class VectorBot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.6)) //driver 1 driving the robot
        addScript(ContinuousMotorScript(id = 4, input = {(gamepad2.right_stick_y).toDouble()})) //vertical slide; motor 1
        addScript(ContinuousMotorScript(id = 5, input = {(-gamepad2.right_stick_y.toDouble())})) //vertical slide; motor 2
        addScript(ContinuousServoScript(id = 0, input = {(-gamepad2.left_stick_y).toDouble()})) //horizontal slide servo 1
        addScript(ContinuousServoScript(id = 1, input = {(gamepad2.left_stick_y).toDouble()})) //horizontal slide servo 2
        addScript(ContinuousServoScript(id = 2, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.cross}))) //claw grabber
        addScript(ContinuousServoScript(id = 5, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.dpad_up}))) //claw raiser
        addScript(ContinuousServoScript(id = 4, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.dpad_right}))) //claw rotator
        addScript(ContinuousServoScript(id = 6, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.right_bumper}, power = 1.0, idle = 0.0))) //transfer claw grabber
        addScript(ContinuousServoScript(id = 7, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.left_bumper}, power = 1.0, idle = -1.0))) //transfer rotator
        addScript(ContinuousMotorScript(id = 6, input = {(gamepad2.right_trigger-gamepad2.left_trigger).toDouble()}))
}

    override fun run() {
    }

    override fun onStop() {
    }
}