package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.AprilTagDetector
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.imu
import org.firstinspires.ftc.teamcode.internals.misc.MecanumDriver
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import kotlin.math.pow

/**
 * This drives towards an AprilTag target.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.AprilTagDetector]
 */
class DriveTowardsAprilTag : OperationMode(), TeleOperation {
    var driver: MecanumDriver? = null
    var detector: AprilTagDetector? = null
    var targetDetected: Boolean = false
    var headingOffset: Double = 0.0
    val cameraLateralOffset: Double = 7.5
    override fun construct() {
        driver = MecanumDriver()
        detector = AprilTagDetector(Devices.camera0)
        registerFeature(detector!!)
        Logging.log("Ensure that the AprilTag is ID 10 (Check the number below the tag itself)")
        Logging.update()
    }

    override fun run() {
        Logging.clear()
        if (!targetDetected) {
            for (detection in detector!!.currentDetections!!) {
                if (detection.id == 10) {
                    headingOffset = -detection.ftcPose.bearing
                    targetDetected = true
                    break
                }
            }
        } else {
            val heading = Math.toDegrees(imu.orientation.x) + headingOffset

            var rotationPower = 20 * (-heading / 10)
            if (heading < 2 && heading > -2) rotationPower = 0.0

            var range = 0.0
            for (detection in detector!!.currentDetections!!) {
                if (detection.id == 10) {
                    range = detection.ftcPose.range
                    break
                }
            }
            range -= cameraLateralOffset

            if (range == 0.0) {
                driver!!.runMecanum(0.0, 0.0, -rotationPower)
            }

            var lateralPower = 50.0
            if (range <= 12) lateralPower = 0.0
            else if (range < 30) lateralPower = 0.108 * (range - 12).pow(2.0) + 15

            driver!!.runMecanum(0.0, lateralPower, -rotationPower)
        }
    }
}
