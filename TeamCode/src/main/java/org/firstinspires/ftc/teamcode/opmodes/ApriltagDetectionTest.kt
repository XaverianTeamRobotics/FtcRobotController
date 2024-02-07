package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;
import org.firstinspires.ftc.teamcode.features.AprilTagDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

import static java.lang.String.format;
import static org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.log;
import static org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.logData;

/**
 * This tests the AprilTag detector and logs detection info.
 * <p>
 * Features: {@link org.firstinspires.ftc.teamcode.features.AprilTagDetector}
 */
public class ApriltagDetectionTest extends OperationMode implements TeleOperation {
    AprilTagDetector detector;
    @Override
    public void construct() {
        detector = new AprilTagDetector(Devices.camera0);
        registerFeature(detector);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        log("# of AprilTags", detector.getCurrentDetections().size());
        for (AprilTagDetection detection : detector.getCurrentDetections()) {
            if (detection.metadata != null) {
                log(format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                log(format("Range %6.1f (inch)", detection.ftcPose.range));
                log(format("Bearing %6.1f (deg)", detection.ftcPose.bearing));
                log(format("Elevation %6.1f (deg)", detection.ftcPose.elevation));
            } else {
                log(format("\n==== (ID %d) Unknown", detection.id));
                log(format("Center %6.0f %6.0f (pixels)", detection.center.x, detection.center.y));
            }
        }
        Logging.update();
    }
}
