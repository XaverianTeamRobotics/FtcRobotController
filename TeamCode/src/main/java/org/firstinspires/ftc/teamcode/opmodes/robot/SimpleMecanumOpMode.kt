package org.firstinspires.ftc.teamcode.opmodes.robot

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.drive.MecanumDriveScript

@TeleOp(name = "Simple Mecanum OpMode", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class SimpleMecanumOpMode: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}