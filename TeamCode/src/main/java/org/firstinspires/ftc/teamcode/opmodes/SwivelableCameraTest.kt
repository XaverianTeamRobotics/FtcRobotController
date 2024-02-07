package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.AprilTagDetector;
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
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
    int desiredServoPos = 50;
    int tagOfInterest = 0;
    boolean downPressed = false;
    boolean upPressed = false;
    double targetImuHeading = 0;
    @Override
    public void construct() {
        translator = new CameraTranslation(7, 6.75, true);
        detector = new AprilTagDetector(Devices.camera0);
        registerFeature(detector);
        Devices.servo3.setPosition(desiredServoPos);
        registerFeature(new NativeMecanumDrivetrain(true));
    }

    @Override
    public void run() {
        double servoPos = Devices.servo3.getPosition();
        boolean detectedTagOfInterest = false;

        if (!servoInMotion) {
            for (AprilTagDetection detection : detector.getCurrentDetections()) {
                servoPos = Devices.servo3.getPosition();
                if (detection.id == tagOfInterest) {
                    targetImuHeading = Devices.imu.getOrientation().getX();
                    detectedTagOfInterest = true;
                    desiredServoPos = (int) translator.centerCameraInServo(servoPos, -detection.ftcPose.bearing);
                    if (desiredServoPos != Math.round(servoPos)) {
                        Devices.servo3.setPosition(desiredServoPos);
                        servoInMotion = true;
                        detector.stop();
                        break;
                    }
                }
            }
        } else if (Math.round(servoPos) == desiredServoPos) {
            servoInMotion = false;
            detector.start();
            waitFor(0.4);
        } else {
            Logging.log("Servo currently in motion.\nDetection Disabled");
        }

        if (!detectedTagOfInterest) {
            Logging.log("Tag of interest not detected.");
            Devices.servo3.setPosition(50);
            desiredServoPos = 50;
        } else Logging.log("Tracking tag of interest.");

        // If the up button is pressed, increment the tag of interest
        if (gamepad1.dpad_up && !upPressed) {
            tagOfInterest++;
            upPressed = true;
        } else if (!gamepad1.dpad_up) upPressed = false;

        // If the down button is pressed, decrement the tag of interest
        if (gamepad1.dpad_down && !downPressed) {
            tagOfInterest--;
            downPressed = true;
        } else if (!gamepad1.dpad_down) downPressed = false;

        if (tagOfInterest < 0) tagOfInterest = 10;
        if (tagOfInterest > 10) tagOfInterest = 0;

        Logging.log("Tag of Interest", tagOfInterest);
        Logging.update();
    }
}
