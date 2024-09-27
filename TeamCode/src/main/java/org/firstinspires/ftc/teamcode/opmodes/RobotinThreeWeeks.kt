package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.IntakeScript
import org.firstinspires.ftc.teamcode.scripts.LifterScript
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript
import org.firstinspires.ftc.teamcode.scripts.ServoLinearSlideScript
import org.firstinspires.ftc.teamcode.scripts.SlideRaiserScript

@TeleOp(name="RobotInThreeWeeks", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class RobotinThreeWeeks: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript())
        addScript(ServoLinearSlideScript())
        addScript(LifterScript())
        addScript(SlideRaiserScript())
        addScript(IntakeScript())

    }

    override fun run() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }
}