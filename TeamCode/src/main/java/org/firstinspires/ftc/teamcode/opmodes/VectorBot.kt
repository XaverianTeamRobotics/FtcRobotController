package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript


@TeleOp(name = "VectorBot", group = BaseOpMode.FULL_GROUP_NAME)
class VectorBot: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(hasBevelGears = true, powerScale = 0.8)) //driver 1 driving the robot
        addScript(ContinuousMotorScript(id = 5, input = {(gamepad2.right_stick_y).toDouble()})) //vertical lifter; motor 1
        addScript(ContinuousMotorScript(id = 4, input = {(-gamepad2.right_stick_y.toDouble())})) //vertical lifter; motor 2
        addScript(ContinuousServoScript(id = 3, input = {(gamepad2.left_stick_y).toDouble()})) //horizontal slide servo 1
        addScript(ContinuousServoScript(id = 4, input = {(-gamepad2.left_stick_y).toDouble()})) //horizontal slide servo 2
        addScript(ContinuousServoScript(id = 0, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.x}))) //claw grabber
        addScript(ContinuousServoScript(id = 1, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.dpad_right}))) //claw rotator
        addScript(ContinuousServoScript(id = 2, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.dpad_up}))) //claw raiser
        addScript(ContinuousServoScript(id = 6, input = ContinuousMotorScript.twoWayToggleInput(input = {gamepad2.circle}, power = 1.0, idle = 0.625))) //transfer claw grabber
}

    override fun run() {
    }

    override fun onStop() {
    }
}