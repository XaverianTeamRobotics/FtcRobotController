package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

/**
 * This detects AprilTags and shows them on the FTC dashboard.
 *
 *
 * Connections: A camera and a computer connected to the dashboard.
 *
 *
 * Controls: Uses the dashboard.
 */
class AprilTagDetector(var cameraName: CameraName) : Feature(), Buildable {
    var currentDetections: MutableList<AprilTagDetection>? = null
    var aprilTag: AprilTagProcessor? = null
    var vision: VisionPortal? = null

    override fun build() {
        // Build AprilTag Detector
        aprilTag = AprilTagProcessor.Builder()
            .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
            .build()

        vision = VisionPortal.Builder()
            .setCamera(Devices.camera0)
            .addProcessor(aprilTag)
            .enableLiveView(true)
            .build()
    }

    override fun loop() {
        currentDetections = aprilTag!!.detections
    }

    fun stop() {
        vision!!.setProcessorEnabled(aprilTag, false)
        currentDetections!!.clear()
    }

    fun start() {
        vision!!.setProcessorEnabled(aprilTag, true)
        currentDetections!!.clear()
    }

    fun resumeStreaming() {
        vision!!.resumeLiveView()
    }

    fun stopStreaming() {
        vision!!.stopLiveView()
    }
}
