package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script
import kotlin.math.abs
import kotlin.math.max

class MecanumDriveScript : Script {
    private val frontLeftMotor: DcMotor
    private val backLeftMotor: DcMotor
    private val frontRightMotor: DcMotor
    private val backRightMotor: DcMotor
    private val hasBevelGears: Boolean
    private val powerScale: Double

    constructor(hasBevelGears: Boolean = true, powerScale: Double = 1.0) {
        frontLeftMotor = motors.get("fl", 0)
        backLeftMotor = motors.get("bl", 1)
        frontRightMotor = motors.get("fr", 2)
        backRightMotor = motors.get("br", 3)
        this.hasBevelGears = hasBevelGears
        this.powerScale = powerScale
    }

    constructor(frontLeftMotor: DcMotor, backLeftMotor: DcMotor, frontRightMotor: DcMotor, backRightMotor: DcMotor, hasBevelGears: Boolean, powerScale: Double) {
        this.frontLeftMotor = frontLeftMotor
        this.backLeftMotor = backLeftMotor
        this.frontRightMotor = frontRightMotor
        this.backRightMotor = backRightMotor
        this.hasBevelGears = hasBevelGears
        this.powerScale = powerScale
    }

    override fun init() {}

    override fun run() {
        while (true) {
            var startT = System.currentTimeMillis()

            val y = -gamepad1.left_stick_y.toDouble()
            val x = gamepad1.left_stick_x.toDouble() * 1.1
            val rx = gamepad1.right_stick_x.toDouble()

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
                RobotLog.w( "MECANUM: DELTA T > 250 (${System.currentTimeMillis() - startT})")
            }
        }
    }

    override fun onStop() {}
}