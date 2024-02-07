package org.firstinspires.ftc.teamcode.opmodes

import android.annotation.SuppressLint
import org.firstinspires.ftc.teamcode.features.AprilTagDetector
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging

/**
 * This tests the AprilTag detector and logs detection info.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.AprilTagDetector]
 */
class ApriltagDetectionTest : OperationMode(), TeleOperation {
    var detector: AprilTagDetector? = null
    override fun construct() {
        detector = AprilTagDetector(Devices.camera0)
        registerFeature(detector!!)
    }


    @SuppressLint("DefaultLocale")
    override fun run() {
        Logging.log("# of AprilTags", detector!!.currentDetections!!.size)
        for (detection in detector!!.currentDetections!!) {
            if (detection.metadata != null) {
                Logging.log(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name))
                Logging.log(String.format("Range %6.1f (inch)", detection.ftcPose.range))
                Logging.log(String.format("Bearing %6.1f (deg)", detection.ftcPose.bearing))
                Logging.log(String.format("Elevation %6.1f (deg)", detection.ftcPose.elevation))
            } else {
                Logging.log(String.format("\n==== (ID %d) Unknown", detection.id))
                Logging.log(String.format("Center %6.0f %6.0f (pixels)", detection.center.x, detection.center.y))
            }
        }
        Logging.update()
    }
}
