package org.firstinspires.ftc.teamcode.opmodes.slides

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.AbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
@Config
class AbsoluteEncoderTuner: BaseOpMode() {
    private lateinit var encoder: AbsoluteEncoder

    override fun construct() {
        encoder = HardwareManager.absoluteEncoders[port]

        telemetry.addLine("This OpMode will tune the absolute encoders")
        telemetry.addLine("To start, move the encoder to the desired beginning position, then press start")
        telemetry.addLine("While you move the encoder, the constants will automatically update")
        telemetry.addLine("If you roll past the end point, the OpMode will fail")

        telemetry.addLine("Selected port: $port")
        telemetry.addLine("To change selected port, open the Dashboard")
        telemetry.update()
    }

    override fun run() {
        telemetry.clear()
        val minVoltage = encoder.readRawVoltage()
        telemetry.addData("Min Voltage", minVoltage)
        var maxVoltage = encoder.readRawVoltage()
        val i = telemetry.addData("Max Voltage", maxVoltage)
        telemetry.update()

        while (opModeIsActive()) {
            val newVoltage = encoder.readRawVoltage()
            if ((newVoltage < minVoltage - 0.25 || newVoltage < maxVoltage - 0.25) && newVoltage < 0.25) {
                telemetry.addLine("Rollover or backwards movement detected. Please restart")
                telemetry.update()
                return
            }

            maxVoltage = newVoltage
            i.setValue(newVoltage)
        }
    }

    override fun onStop() {

    }

    companion object {
        @JvmStatic
        var port: Int = 0
    }
}