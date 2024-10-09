package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script
import kotlin.math.abs
import kotlin.math.max

class MecanumDriveScript(private val powerScale: Double = 1.0, private val rotScale: Double = 1.0) : Script() {
    private val frontLeftMotor: DcMotor = motors.get("fl", 0)
    private val backLeftMotor: DcMotor = motors.get("bl", 1)
    private val frontRightMotor: DcMotor = motors.get("fr", 2)
    private val backRightMotor: DcMotor = motors.get("br", 3)

    override fun init() {}

    override fun run() {
        while (true) {
            val y = -gamepad1.left_stick_y.toDouble() * powerScale
            val x = gamepad1.left_stick_x.toDouble() * 1.1 * powerScale
            val rx = gamepad1.right_stick_x.toDouble() * powerScale * rotScale

            val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
            val frontLeftPower = (y + x + rx) / denominator
            val backLeftPower = (y - x + rx) / denominator
            val frontRightPower = (y - x - rx) / denominator
            val backRightPower = (y + x - rx) / denominator

            frontLeftMotor.power = frontLeftPower
            backLeftMotor.power = backLeftPower
            frontRightMotor.power = frontRightPower
            backRightMotor.power = backRightPower
        }
    }

    override fun onStop() {}
}