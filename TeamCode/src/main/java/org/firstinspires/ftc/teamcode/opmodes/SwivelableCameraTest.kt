package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.AprilTagDetector
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.imu
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo3
import org.firstinspires.ftc.teamcode.internals.image.CameraTranslation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging

/**
 * Uses servo 3
 */
class SwivelableCameraTest : OperationMode(), TeleOperation {
    var translator: CameraTranslation? = null
    var detector: AprilTagDetector? = null
    var servoInMotion: Boolean = false
    var desiredServoPos: Int = 50
    var tagOfInterest: Int = 0
    var downPressed: Boolean = false
    var upPressed: Boolean = false
    var targetImuHeading: Double = 0.0
    override fun construct() {
        translator = CameraTranslation(7.0, 6.75, true)
        detector = AprilTagDetector(Devices.camera0)
        registerFeature(detector!!)
        servo3.position = desiredServoPos.toDouble()
        registerFeature(NativeMecanumDrivetrain(true))
    }

    override fun run() {
        var servoPos = servo3.position
        var detectedTagOfInterest = false

        if (!servoInMotion) {
            for (detection in detector!!.currentDetections!!) {
                servoPos = servo3.position
                if (detection.id == tagOfInterest) {
                    targetImuHeading = imu.orientation.x
                    detectedTagOfInterest = true
                    desiredServoPos = translator!!.centerCameraInServo(servoPos, -detection.ftcPose.bearing).toInt()
                    if (desiredServoPos.toLong() != Math.round(servoPos)) {
                        servo3.position = desiredServoPos.toDouble()
                        servoInMotion = true
                        detector!!.stop()
                        break
                    }
                }
            }
        } else if (Math.round(servoPos) == desiredServoPos.toLong()) {
            servoInMotion = false
            detector!!.start()
            waitFor(0.4)
        } else {
            Logging.log("Servo currently in motion.\nDetection Disabled")
        }

        if (!detectedTagOfInterest) {
            Logging.log("Tag of interest not detected.")
            servo3.position = 50.0
            desiredServoPos = 50
        } else Logging.log("Tracking tag of interest.")

        // If the up button is pressed, increment the tag of interest
        if (gamepad1.dpad_up && !upPressed) {
            tagOfInterest++
            upPressed = true
        } else if (!gamepad1.dpad_up) upPressed = false

        // If the down button is pressed, decrement the tag of interest
        if (gamepad1.dpad_down && !downPressed) {
            tagOfInterest--
            downPressed = true
        } else if (!gamepad1.dpad_down) downPressed = false

        if (tagOfInterest < 0) tagOfInterest = 10
        if (tagOfInterest > 10) tagOfInterest = 0

        Logging.log("Tag of Interest", tagOfInterest)
        Logging.update()
    }
}
