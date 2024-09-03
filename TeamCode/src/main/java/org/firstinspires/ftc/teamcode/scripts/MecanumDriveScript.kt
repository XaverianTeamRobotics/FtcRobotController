package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.Script
import org.firstinspires.ftc.teamcode.internals.dynamicmapping.*
import kotlin.math.abs
import kotlin.math.max

@UsesDynamicMapping
class MecanumDriveScript : Script {
    @DynamicMappingField(hardwareType = DynamicMappingHardware.MOTOR, default = 0)
    private val frontLeftMotor: DcMotor
    @DynamicMappingField(hardwareType = DynamicMappingHardware.MOTOR, default = 1)
    private val backLeftMotor: DcMotor
    @DynamicMappingField(hardwareType = DynamicMappingHardware.MOTOR, default = 2)
    private val frontRightMotor: DcMotor
    @DynamicMappingField(hardwareType = DynamicMappingHardware.MOTOR, default = 3)
    private val backRightMotor: DcMotor

    constructor() {
        val dynamicMapping = DynamicMappingManager.applyDynamicMapping(this::class)
        frontLeftMotor = dynamicMapping.frontLeftMotor
        backLeftMotor = dynamicMapping.backLeftMotor
        frontRightMotor = dynamicMapping.frontRightMotor
        backRightMotor = dynamicMapping.backRightMotor
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
            val y = gamepad1.left_stick_y.toDouble()
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