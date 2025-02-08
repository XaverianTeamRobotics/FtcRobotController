package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
class DistanceSensorLogging: BaseOpMode() {
    override fun construct() {

    }

    override fun run() {
        while (opModeIsActive()) {
            telemetry.addData("Distance", HardwareManager.distanceSensor[0].getDistance(DistanceUnit.INCH))
            telemetry.update()
        }
    }

    override fun onStop() {

    }
}