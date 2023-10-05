package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.AprilTagDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.image.CameraTranslation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

/**
 * Uses servo 3
 */
public class SwivelableCameraTest extends OperationMode implements TeleOperation {
    CameraTranslation translator;
    AprilTagDetector detector;
    boolean servoInMotion = false;
    double desiredServoPos = 50;
    @Override
    public void construct() {
        translator = new CameraTranslation(7, 6.75, true);
        detector = new AprilTagDetector(Devices.camera0);
        registerFeature(detector);
        Devices.servo3.setPosition(desiredServoPos);
    }

    @Override
    public void run() {
        double servoPos = Devices.servo3.getPosition();
        Logging.log("Servo Position", servoPos);
        Logging.log("\n");
        for (AprilTagDetection detection: detector.getCurrentDetections()) {
            Logging.log("Tag ID", detection.id);
            Logging.log("---Camera Relative---");
            Logging.log("     Angle", -detection.ftcPose.bearing);
            Logging.log("     Distance", detection.ftcPose.range);
            Logging.log("\n");
            Logging.log("---Robot Relative---");
            Logging.log("     Angle", translator.convertCameraBearingAndRangeToRobotCentric(servoPos, -detection.ftcPose.bearing, detection.ftcPose.range)[0]);
            Logging.log("     Distance", translator.convertCameraBearingAndRangeToRobotCentric(servoPos, -detection.ftcPose.bearing, detection.ftcPose.range)[1]);
            Logging.log("\n");

            servoPos = Devices.servo3.getPosition();
            if ((int) servoPos == (int) desiredServoPos) {
                servoInMotion = false;
            }
            if (detection.id == 3 && !servoInMotion) {
                desiredServoPos = translator.centerCameraInServo(servoPos, -detection.ftcPose.bearing);
                Devices.servo3.setPosition(desiredServoPos);
                servoInMotion = true;
            }
        }
        Logging.update();
    }
}
