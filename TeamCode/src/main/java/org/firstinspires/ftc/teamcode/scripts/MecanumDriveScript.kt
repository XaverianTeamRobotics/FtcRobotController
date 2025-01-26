package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.templates.Script
import kotlin.math.abs
import kotlin.math.max

/**
 * Script for controlling a mecanum drive robot.
 *
 * @property hasBevelGears Boolean indicating if the robot has bevel gears.
 * @property powerScale Double value to scale the motor power.
 * @property rotScale Double value to scale the rotation.
 */
class MecanumDriveScript(
    private val hasBevelGears: Boolean = true,
    private val powerScale: Double = 1.0,
    private val rotScale: Double = 1.0,
    private val lateralInput: () -> Double = { gamepad1.left_stick_x.toDouble() },
    private val forwardInput: () -> Double = { -gamepad1.left_stick_y.toDouble() },
    private val rotationInput: () -> Double = { gamepad1.right_stick_x.toDouble() }
) : Script() {
    private val frontLeftMotor: DcMotor = motors.get("fl", 0)
    private val backLeftMotor: DcMotor = motors.get("bl", 1)
    private val frontRightMotor: DcMotor = motors.get("fr", 2)
    private val backRightMotor: DcMotor = motors.get("br", 3)

    override fun init() {}

    override fun run() {
        try {
            while (scriptIsActive()) {
                var startT = System.currentTimeMillis()

                val y = forwardInput()
                val x = lateralInput() * 1.1
                val rx = rotationInput() * rotScale

                val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
                var frontLeftPower = (y + x + rx) / denominator
                var backLeftPower = (y - x + rx) / denominator
                var frontRightPower = (y - x - rx) / denominator
                var backRightPower = (y + x - rx) / denominator

                if (hasBevelGears) {
                    frontRightPower = -frontRightPower
                    frontLeftPower = -frontLeftPower
                    backLeftPower = -backLeftPower
                    backRightPower = -backRightPower
                }

                frontLeftMotor.power = frontLeftPower * powerScale
                backLeftMotor.power = backLeftPower * powerScale
                frontRightMotor.power = -(frontRightPower * powerScale)
                backRightMotor.power = -(backRightPower * powerScale)

                if (System.currentTimeMillis() - startT > 250) {
                    RobotLog.w("MECANUM: DELTA T > 250 (${System.currentTimeMillis() - startT})")
                }
            }
        } catch (e: Exception) {
            RobotLog.e("MecanumDriveScript: ${e.message}")
            return
        }
    }

    override fun onStop() {}
}