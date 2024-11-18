package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name="RobotInThreeWeeks", group = BaseOpMode.FULL_GROUP_NAME)
class RobotInThreeWeeks: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(powerScale = 0.8))
        addScript(ContinuousServoScript(inverted = true)) // Slide
        addScript(ContinuousServoScript(
            id = 1, input = ContinuousMotorScript.threeWayInput(
            {gamepad1.y}, {gamepad1.a}, {gamepad1.b} // Intake
        )))
        addScript(ContinuousMotorScript(id = 2, input = ContinuousMotorScript.twoButtonInput(
            {gamepad1.right_bumper}, {gamepad1.left_bumper} // Lifters
        )))
        addScript(ContinuousMotorScript(id = 3, input = ContinuousMotorScript.twoButtonInput(
            {gamepad1.dpad_down}, {gamepad1.dpad_up}, pow = 0.5 // Tilting
        )))
    }

    override fun run() {

    }

    override fun onStop() {

    }
}