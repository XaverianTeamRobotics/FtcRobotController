package org.firstinspires.ftc.teamcode.scripts.auto

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.templates.Script

/**
 * Script for logging the position data from the Limelight 3A vision sensor.
 */
class LimelightPositionLogging: Script() {
    /**
     * Initializes the script. Sets the telemetry transmission interval and starts the Limelight 3A sensor.
     */
    override fun init() {
        telemetry.msTransmissionInterval = 11
        limelight3A?.pipelineSwitch(0)
        limelight3A?.pipelineSwitch(3)
        limelight3A?.start()
    }

    /**
     * Main loop for logging position data from the Limelight 3A sensor.
     * Continuously checks for valid results and logs the data to telemetry.
     * (All units in meters)
     * [See more here](https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-coordinate-systems)
     *
     * **(don't ask me what orthogonal means)**
     */
    override fun run() {
        while (scriptIsActive()) {
            val result = limelight3A?.latestResult
            if (result != null) {
                if (result.isValid) {
                    telemetry.clear()
                    val botpose = result.botpose // The position of the bot (assuming apriltags set up correctly)
                    val fiducials = result.fiducialResults
                    telemetry.addData("tx", result.tx)
                    telemetry.addData("ty", result.ty)

                    for (i in fiducials) {
                        telemetry.addData("Tag ID", i.fiducialId)
                        val cp = i.cameraPoseTargetSpace // the camera as a moving object, target is fixed
                        val tp = i.targetPoseCameraSpace // the target as a moving object, camera is fixed
                        telemetry.addLine("\tCamera Pose")
                        telemetry.addData("\t\tX", cp.position.x)
                        telemetry.addData("\t\tY", cp.position.y)
                        telemetry.addData("\t\tZ", cp.position.z)

                        telemetry.addLine("\tTarget Pose")
                        telemetry.addData("\t\tX", tp.position.x)
                        telemetry.addData("\t\tY", tp.position.y)
                        telemetry.addData("\t\tZ", tp.position.z)

                        telemetry.addLine()
                    }

                    telemetry.addData("Botpose", botpose.toString())
                }
            }
            telemetry.update()
        }
    }

    /**
     * Called when the script is stopped. Currently, this method does nothing.
     */
    override fun onStop() {

    }
}