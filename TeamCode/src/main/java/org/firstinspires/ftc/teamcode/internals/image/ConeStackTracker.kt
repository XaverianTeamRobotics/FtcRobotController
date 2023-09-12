package org.firstinspires.ftc.teamcode.internals.image

import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.hardwareMap
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.logData
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.updateLog
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.tan

class ConeStackTracker(val isBlueTeam: Boolean, val enableDisplayOfAngles: Boolean): OpenCvPipeline() {
    var detectedCones: MutableList<KeyPoint> = mutableListOf()
    override fun processFrame(input: Mat): Mat {

        //AdvancedLogging.logData("Input Height", input.height());
        //AdvancedLogging.logData("Input Width", input.width());

        // Blur the image to reduce noise
        val blurSize = Size(55.0,55.0)
        Imgproc.GaussianBlur(input, input, blurSize, 0.0)

        // Convert to HSV
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV)

        // Define some colors

        val blueLowerBound = Scalar(ImageProcessingConstants.BLUE_H_MIN, ImageProcessingConstants.BLUE_S_MIN, ImageProcessingConstants.BLUE_V_MIN)
        val blueUpperBound = Scalar(ImageProcessingConstants.BLUE_H_MAX, ImageProcessingConstants.BLUE_S_MAX, ImageProcessingConstants.BLUE_V_MAX)

        val redLowerBound = Scalar(ImageProcessingConstants.RED_H_MIN, ImageProcessingConstants.RED_S_MIN, ImageProcessingConstants.RED_V_MIN)
        val redUpperBound = Scalar(ImageProcessingConstants.RED_H_MAX, ImageProcessingConstants.RED_S_MAX, ImageProcessingConstants.RED_V_MAX)

        // Apply an edge detector to just get the cones
        val lowerBound = if (isBlueTeam) blueLowerBound else redLowerBound
        val upperBound = if (isBlueTeam) blueUpperBound else redUpperBound
        val mask1 = Mat()
        Core.inRange(input, lowerBound, upperBound, mask1)
        Imgproc.cvtColor(input, input, Imgproc.COLOR_HSV2RGB)
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY)
        Core.bitwise_and(input, mask1, input)

        // Run a Canny edge detector
        Imgproc.Canny(input, input, ImageProcessingConstants.CANNY_1, ImageProcessingConstants.CANNY_2)

        // Detect the areas within the edges created by the Canny edge detector
        // Find the contours in the image
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(input, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        detectedCones.clear()
        // Iterate through the contours
        for (contour in contours) {
            // Approximate the contour as a polygon
            val points = MatOfPoint2f()
            points.fromList(contour.toList().toMutableList())
            val epsilon = 0.01 * Imgproc.arcLength(points, true)
            points.release()
            val approx = MatOfPoint2f()
            Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, epsilon, true)

            // Create a keypoint using a rectangle that fits the polygon
            val rect = Imgproc.boundingRect(approx)
            val keyPoint = KeyPoint(
                (rect.x + rect.width / 2.0).toFloat(),      // x
                (rect.y + rect.height / 2.0).toFloat(),     // y
                rect.width.toDouble().toFloat()             // size
            )

            detectedCones.add(keyPoint)
        }

        // Draw the keypoints on the image as boxes with a number in them
        val color = Scalar(255.0, 255.0, 255.0)
        for (i in detectedCones.indices) {
            val keypoint = detectedCones[i]
            val rect = Rect(keypoint.pt.x.toInt() - 10, keypoint.pt.y.toInt() - 10, 20, 20)
            Imgproc.rectangle(input, rect, color, 2)
            var text = "#$i"
            if (enableDisplayOfAngles) {
                val angle = getAngleToTarget(keypoint)
                text += " ${angle.toInt()}*"
            }
            Imgproc.putText(input, text, keypoint.pt, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, color, 2)
        }

        // Clean up and return the output
        Imgproc.cvtColor(input, input, Imgproc.COLOR_GRAY2RGB)
        return input
    }

    fun startStreaming(): ConeStackTracker {
        val cameraMonitorViewId = hardwareMap!!.appContext?.resources?.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap!!.appContext.packageName
        )
        val camera: OpenCvCamera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0,
            cameraMonitorViewId!!
        )

        camera.setPipeline(this)
        FtcDashboard.getInstance().startCameraStream(camera, 0.0)

        camera.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                /*
                 * This will be called if the camera could not be opened
                 */
                logData("Camera error", errorCode)
                updateLog()
            }
        })

        return this
    }

    fun getAngleToTarget(keyPoint: KeyPoint): Int {
        val center = keyPoint.pt
        return (Math.toDegrees(atan2(center.x - 160, 160 / tan(Math.toRadians(60.0)))) * (1.0/3.0)).roundToInt()
    }

    fun returnBiggestCone(): KeyPoint? {
        var biggestCone: KeyPoint? = null
        for (cone in detectedCones) {
            if (biggestCone == null || cone.size > biggestCone.size) {
                biggestCone = cone
            }
        }
        return biggestCone
    }
}