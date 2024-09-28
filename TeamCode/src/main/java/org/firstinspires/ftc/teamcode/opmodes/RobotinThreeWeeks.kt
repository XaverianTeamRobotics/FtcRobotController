package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(name="RobotInThreeWeeks", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class RobotinThreeWeeks: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript())
        addScript(ContinuousServoScript())
//        addScript(LifterScript())
//        addScript(SlideRaiserScript())
//        addScript(IntakeScript())
        addScript(ContinuousServoScript(id = 1, input = ContinuousMotorScript.threeWayInput(
            {gamepad1.y}, {gamepad1.a}, {gamepad1.b}
        )))
        addScript(ContinuousMotorScript(id = 2, input = ContinuousMotorScript.twoButtonInbut(
            {gamepad1.right_bumper}, {gamepad1.left_bumper}
        )))
        addScript(ContinuousMotorScript(id = 3, input = ContinuousMotorScript.twoButtonInbut(
            {gamepad1.dpad_up}, {gamepad1.dpad_down}
        )))
    }

    override fun run() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }
}