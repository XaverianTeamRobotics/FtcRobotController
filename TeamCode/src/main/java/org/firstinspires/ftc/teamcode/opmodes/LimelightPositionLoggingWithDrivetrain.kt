package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.LimelightPositionLogging
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript

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