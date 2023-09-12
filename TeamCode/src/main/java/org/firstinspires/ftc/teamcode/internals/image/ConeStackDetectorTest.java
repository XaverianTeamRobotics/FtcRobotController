package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging;
import org.opencv.core.KeyPoint;

public class ConeStackDetectorTest extends OperationMode implements AutonomousOperation {
    ConeStackTracker detector;

    @Override
    public void construct() {
        detector = new ConeStackTracker(false, true);
        detector.startStreaming();
    }

    @Override
    public void run() {
        // Print every detected cone
        for (KeyPoint keypoint : detector.getDetectedCones()) {
            AdvancedLogging.logText(keypoint.pt.toString());
            AdvancedLogging.logText(String.valueOf(detector.getAngleToTarget(keypoint)));
        }
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }
}