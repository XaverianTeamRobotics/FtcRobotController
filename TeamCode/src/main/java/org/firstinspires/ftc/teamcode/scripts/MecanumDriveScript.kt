package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
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

    constructor() {
        frontLeftMotor = motors.get("fl", 0)
        backLeftMotor = motors.get("bl", 1)
        frontRightMotor = motors.get("fr", 2)
        backRightMotor = motors.get("br", 3)
    }

    constructor(frontLeftMotor: DcMotor, backLeftMotor: DcMotor, frontRightMotor: DcMotor, backRightMotor: DcMotor) {
        this.frontLeftMotor = frontLeftMotor
        this.backLeftMotor = backLeftMotor
        this.frontRightMotor = frontRightMotor
        this.backRightMotor = backRightMotor
    }

    override fun init() {}

    override fun run() {
        while (true) {
            val y = -gamepad1.left_stick_y.toDouble()
            val x = gamepad1.left_stick_x.toDouble() * 1.1
            val rx = gamepad1.right_stick_x.toDouble()

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