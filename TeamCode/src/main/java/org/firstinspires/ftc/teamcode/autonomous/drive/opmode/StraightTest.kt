package org.firstinspires.ftc.teamcode.autonomous.drive.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.autonomous.drive.SampleMecanumDrive

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
class StraightTest : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        val telemetry: Telemetry = MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry())

        val drive = SampleMecanumDrive(hardwareMap)

        val trajectory = drive.trajectoryBuilder(Pose2d())
            .forward(DISTANCE)
            .build()

        waitForStart()

        if (isStopRequested()) return

        drive.followTrajectory(trajectory)

        val poseEstimate = drive.poseEstimate
        telemetry.addData("finalX", poseEstimate.x)
        telemetry.addData("finalY", poseEstimate.y)
        telemetry.addData("finalHeading", poseEstimate.heading)
        telemetry.update()

        while (!isStopRequested() && opModeIsActive());
    }

    companion object {
        var DISTANCE: Double = 60.0 // in
    }
}
