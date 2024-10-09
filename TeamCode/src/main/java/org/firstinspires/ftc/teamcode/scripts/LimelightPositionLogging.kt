package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.limelight3A
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.base.Script

class LimelightPositionLogging: Script() {
    override fun init() {
        telemetry.msTransmissionInterval = 11
        limelight3A?.pipelineSwitch(0)
        limelight3A?.pipelineSwitch(3)
        limelight3A?.start()
    }

    override fun run() {
        while (scriptIsActive()) {
            val result = limelight3A?.latestResult
            if (result != null) {
                if (result.isValid) {
                    telemetry.clear()
                    val botpose = result.botpose
                    val fiducials = result.fiducialResults
                    telemetry.addData("tx", result.tx)
                    telemetry.addData("ty", result.ty)

                    for (i in fiducials) {
                        telemetry.addData("Tag ID", i.fiducialId)
                        val cp = i.cameraPoseTargetSpace
                        val tp = i.targetPoseCameraSpace
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

    override fun onStop() {

    }
}