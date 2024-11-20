package org.firstinspires.ftc.teamcode.autonomous.opmodes.tuning

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.autonomous.drive.MecanumDriver

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Autonomous(group = "drive")
class TurnTest : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        val drive = MecanumDriver(hardwareMap)

        waitForStart()

        if (isStopRequested()) return

        drive.turn(Math.toRadians(ANGLE))
    }

    companion object {
        var ANGLE: Double = 90.0 // deg
    }
}
