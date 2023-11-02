package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagDetector extends Feature implements Buildable {
    AprilTagProcessor aprilTag;
    VisionPortal vision;
    public List<AprilTagDetection> currentDetections;
    CameraName cameraName;

    public AprilTagDetector(CameraName cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public void build() {
        // Build AprilTag Detector
        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        vision = new VisionPortal.Builder()
                .setCamera(Devices.camera0)
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .build();
    }

    @Override
    public void loop() {
        currentDetections = aprilTag.getDetections();
    }

    public List<AprilTagDetection> getCurrentDetections() { return currentDetections; }

    public void stop() {
        vision.setProcessorEnabled(aprilTag, false);
        currentDetections.clear();
    }

    public void start() {
        vision.setProcessorEnabled(aprilTag, true);
        currentDetections.clear();
    }

    public void resumeStreaming() {
        vision.resumeLiveView();
    }

    public void stopStreaming() {
        vision.stopLiveView();
    }
}
