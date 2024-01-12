package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.rrqs;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoader;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoaderFailureException;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

import static java.lang.Math.toRadians;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
//@Disabled
@TeleOp(group = "drive")
public class LocalizationTestBlueLeftStart extends OperationMode {
    private AutonomousDrivetrain drive;

    @Override
    public void construct() {
        drive = new AutonomousDrivetrain(hardwareMap);
        drive.setPoseEstimate(new Pose2d(AutoAutoPathSegment.START_L_X, AutoAutoPathSegment.START_L_Y, toRadians(-90)));
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void run() {
        drive.setWeightedDrivePower(
                new Pose2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x
                )
        );

        drive.update();

        Pose2d poseEstimate = drive.getPoseEstimate();
        Logging.log("x", poseEstimate.getX());
        Logging.log("y", poseEstimate.getY());
        Logging.log("heading", poseEstimate.getHeading());
        if (gamepad1.a) {
            drive.setWeightedDrivePower(new Pose2d(AutoAutoPathSegment.START_L_X, AutoAutoPathSegment.START_L_Y, toRadians(-90)));
            drive.update();

            Logging.log("Saving...");
            Logging.update();
            Clock.sleep(1000);
            Logging.clear();
            Logging.update();
            try {
                SettingLoader.save();
                Logging.log("Saved");
                Logging.update();
                Clock.sleep(1000);
            } catch(SettingLoaderFailureException e) {
                System.out.println("Saving settings failed! " + e.getMessage());
                Logging.log("Couldn't save settings! Check logcat for more details.");
                Logging.update();
                e.printStackTrace();
                System.out.println(e.toString());
                Clock.sleep(1000);
            }
        }
        if (gamepad1.y) {
            drive.setWeightedDrivePower(new Pose2d(AutoAutoPathSegment.START_L_X, AutoAutoPathSegment.START_L_Y, toRadians(-90)));
            drive.update();
            drive = new AutonomousDrivetrain(hardwareMap);
            drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            Logging.log("Reset config");
            Logging.update();
            Clock.sleep(1000);
        }
        Logging.update();
    }
}
