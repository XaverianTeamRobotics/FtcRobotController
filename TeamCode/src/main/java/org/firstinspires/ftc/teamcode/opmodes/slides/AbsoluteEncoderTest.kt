package org.firstinspires.ftc.teamcode.opmodes.slides

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.AbsoluteEncoder
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp
class AbsoluteEncoderTest: BaseOpMode() {
    lateinit var encoder: AbsoluteEncoder
    lateinit var motor: DcMotor
    override fun construct() {
        encoder = HardwareManager.absoluteEncoders[0]
        motor = HardwareManager.motors[0]
    }

    override fun run() {
        while (opModeIsActive()) {
            telemetry.addData("Pos", encoder.position)
            telemetry.addData("Encoder", motor.currentPosition)
            telemetry.update()
        }
    }

    override fun onStop() {

    }
}