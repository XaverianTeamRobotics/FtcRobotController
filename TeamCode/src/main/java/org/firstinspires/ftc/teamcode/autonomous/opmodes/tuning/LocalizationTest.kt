package org.firstinspires.ftc.teamcode.autonomous.opmodes.tuning

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.autonomous.drive.MecanumDriver
import org.firstinspires.ftc.teamcode.autonomous.limelight.LimelightServoScript
import org.firstinspires.ftc.teamcode.autonomous.localizers.LimelightLocalizer
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.templates.initHardwareManager
import java.lang.Math.toDegrees

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
class LocalizationTest : BaseOpMode() {
    lateinit var drive: MecanumDriver

    override fun construct() {
        drive = MecanumDriver(hardwareMap)

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        addScript(LimelightServoScript({LimelightServoScript.LimelightServoPosition.CENTER}))
    }

    override fun run() {
        while (!isStopRequested) {
            drive.setWeightedDrivePower(
                Pose2d(
                    -gamepad1.left_stick_y.toDouble(),
                    -gamepad1.left_stick_x.toDouble(),
                    -gamepad1.right_stick_x.toDouble()
                )
            )

            drive.update()

            val poseEstimate = drive.poseEstimate
            telemetry.addData("x", poseEstimate.x)
            telemetry.addData("y", poseEstimate.y)
            telemetry.addData("heading (deg)", toDegrees(poseEstimate.heading))
            telemetry.addData("pinpointRefreshRate", drive.ppl.pinpoint.loopTime)
            telemetry.update()

            sleep(50)
        }
    }

    override fun onStop() {

    }
}
