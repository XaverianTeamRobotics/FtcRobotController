package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.auto.LimelightPositionLogging
import org.firstinspires.ftc.teamcode.scripts.drive.MecanumDriveScript

@TeleOp(name="Limelight Position Logging With Drivetrain", group = BaseOpMode.DEBUG_GROUP_NAME)
class LimelightPositionLoggingWithDrivetrain: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(rotScale = 0.75, powerScale = 0.75))
        addScript(LimelightPositionLogging())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}