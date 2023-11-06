package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.AprilTagDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.MecanumDriver;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

/**
 * This drives towards an AprilTag target.
 * <p>
 * Features: @see AprilTagDetector
 */
public class DriveTowardsAprilTag extends OperationMode implements TeleOperation {
    MecanumDriver driver;
    AprilTagDetector detector;
    boolean targetDetected = false;
    double headingOffset = 0.0;
    final double cameraLateralOffset = 7.5;
    @Override
    public void construct() {
        driver = new MecanumDriver();
        detector = new AprilTagDetector(Devices.camera0);
        registerFeature(detector);
        Logging.log("Ensure that the AprilTag is ID 10 (Check the number below the tag itself)");
        Logging.update();
    }

    @Override
    public void run() {
        Logging.clear();
        if (!targetDetected) {
            for (AprilTagDetection detection : detector.getCurrentDetections()) {
                if (detection.id == 10) {
                    headingOffset = -detection.ftcPose.bearing;
                    targetDetected = true;
                    break;
                }
            }
        } else {
            double heading = Math.toDegrees(Devices.imu.getOrientation().getX()) + headingOffset;

            double rotationPower = 20 * (-heading / 10);
            if (heading < 2 && heading > -2) rotationPower = 0;

            double range = 0.0;
            for (AprilTagDetection detection : detector.getCurrentDetections()) {
                if (detection.id == 10) {
                    range = detection.ftcPose.range;
                    break;
                }
            }
            range -= cameraLateralOffset;

            if (range == 0.0) {
                driver.runMecanum(0, 0, -rotationPower);
            }

            double lateralPower = 50.0;
            if (range <= 12) lateralPower = 0;
            else if (range < 30) lateralPower = 0.108 * Math.pow(range - 12, 2) + 15;

            driver.runMecanum(0, lateralPower, -rotationPower);
        }

    }
}
