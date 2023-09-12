package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import static org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.log;
import static org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.logData;

public class ApriltagDetectionTest extends OperationMode implements TeleOperation {
    AprilTagProcessor aprilTag;
    VisionPortal vision;
    @Override
    public void construct() {
        // Build AprilTag Detector
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();

        vision = new VisionPortal.Builder()
                .setCamera(Devices.camera0)
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .build();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        logData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                log(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                log(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                log(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                log(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                log(String.format("\n==== (ID %d) Unknown", detection.id));
                log(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        log("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        log("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        log("RBE = Range, Bearing & Elevation");
    }
}
