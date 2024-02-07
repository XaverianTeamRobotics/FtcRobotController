package org.firstinspires.ftc.teamcode.features

import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.hardwareMap
import org.firstinspires.ftc.teamcode.internals.image.MultipleCameraManager
import org.firstinspires.ftc.teamcode.internals.image.powerplay.SleeveColorDetection
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.logData
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.update
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import java.util.*

class SleeveDetector : Feature, Buildable {
    private var detector: SleeveColorDetection? = null

    var spot: Int = 0
        private set
    private val previousSpots = ArrayList<Int>()
    var averageSpot: Int = 0
        private set
    var isReady: Boolean = false
        private set
    private var indexed = false
    private var index = 0

    /**
     * Use this when only using one camera at a time.
     */
    constructor()

    /**
     * Use this when two cameras are streaming concurrently.
     * @param index The index of the camera, either 0 or 1.
     */
    constructor(index: Int) {
        indexed = true
        this.index = index
    }

    override fun build() {
        val cameraMonitorViewId = hardwareMap!!.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap!!.appContext.packageName
        )

        var viewportContainerIds: IntArray? = null
        if (indexed) {
            viewportContainerIds = MultipleCameraManager.get(cameraMonitorViewId)
        }

        val camera: OpenCvCamera = if (indexed) {
            OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0, viewportContainerIds!![index])
        } else {
            OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0, cameraMonitorViewId)
        }

        detector = SleeveColorDetection()

        detector!!.isDebugEnabled = false

        camera.setPipeline(detector)
        FtcDashboard.getInstance().startCameraStream(camera, 0.0)

        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                isReady = true
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                /*
                 * This will be called if the camera could not be opened
                 */

                logData("Camera error", errorCode)
                update()
            }
        })
    }

    override fun loop() {
        spot = detector!!.detection
        previousSpots.add(spot)
        // Get the average spot out of all spots, then round it
        averageSpot =
            Math.round(previousSpots.stream().mapToInt { obj: Int -> obj.toInt() }.average().orElse(0.0)).toInt()
    }

    fun setDebugEnabled(enabled: Boolean) {
        detector!!.isDebugEnabled = enabled
    }
}
