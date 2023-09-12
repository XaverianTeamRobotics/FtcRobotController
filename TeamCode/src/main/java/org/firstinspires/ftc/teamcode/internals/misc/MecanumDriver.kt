package org.firstinspires.ftc.teamcode.internals.misc

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class MecanumDriver(val drivetrainMapMode: DrivetrainMapMode, val useExpansionHub: Boolean, val fieldCentric: Boolean) {
    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false)

    fun runMecanum(x: Double, y: Double, rot: Double) {
        var backLeftPower: Double      = 0.0
        var frontLeftPower: Double     = 0.0
        var backRightPower: Double     = 0.0
        var frontRightPower: Double    = 0.0

        when (fieldCentric) {
            false -> {
                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(rot) + abs(x) + abs(y), 1.0)
                frontLeftPower = (y + x + rot) / denominator
                backLeftPower = (y - x + rot) / denominator
                frontRightPower = -(y - x - rot) / denominator
                backRightPower = -(y + x - rot) / denominator
            }
            true -> {
                // Read inverse IMU heading, as the IMU heading is CW positive
                val botHeading: Double = -Devices.imu.orientation.x

                val rotX = x * cos(botHeading) - y * sin(botHeading)
                val rotY = x * sin(botHeading) + y * cos(botHeading)

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(y) + abs(x) + abs(rot), 1.0)
                frontLeftPower = (rotY + rotX + rot) / denominator
                backLeftPower = (rotY - rotX + rot) / denominator
                frontRightPower = -(rotY - rotX - rot) / denominator
                backRightPower = -(rotY + rotX - rot) / denominator
            }
        }

        when (useExpansionHub) {
            false -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        Devices.motor0.power = frontRightPower * 100
                        Devices.motor1.power = backRightPower * 100
                        Devices.motor2.power = frontLeftPower * 100
                        Devices.motor3.power = backLeftPower * 100
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        Devices.motor0.power = backLeftPower * 100
                        Devices.motor1.power = frontLeftPower * 100
                        Devices.motor2.power = backRightPower * 100
                        Devices.motor3.power = frontRightPower * 100
                    }
                }
            }

            true -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        Devices.motor4.power = frontRightPower * 100
                        Devices.motor5.power = backRightPower * 100
                        Devices.motor6.power = frontLeftPower * 100
                        Devices.motor7.power = backLeftPower * 100
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        Devices.motor4.power = backLeftPower * 100
                        Devices.motor5.power = frontLeftPower * 100
                        Devices.motor6.power = backRightPower * 100
                        Devices.motor7.power = frontRightPower * 100
                    }
                }
            }
        }
    }

    fun getMecanumFormulaOutput(x: Double, y: Double, rot: Double): MecanumOutput {
        val backLeftPower: Double
        val frontLeftPower: Double
        val backRightPower: Double
        val frontRightPower: Double

        when (fieldCentric) {
            false -> {
                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(rot) + abs(x) + abs(y), 1.0)
                frontLeftPower = (y + x + rot) / denominator
                backLeftPower = (y - x + rot) / denominator
                frontRightPower = -(y - x - rot) / denominator
                backRightPower = -(y + x - rot) / denominator
            }

            true -> {
                // Read inverse IMU heading, as the IMU heading is CW positive
                val botHeading: Double = -Devices.imu.orientation.x

                DSLogging.log("Bot Heading", botHeading)
                DSLogging.log("   X", Devices.imu.orientation.x)
                DSLogging.log("   Y", Devices.imu.orientation.y)
                DSLogging.log("   Z", Devices.imu.orientation.z)
                DSLogging.update()

                val rotX = x * cos(botHeading) - y * sin(botHeading)
                val rotY = x * sin(botHeading) + y * cos(botHeading)

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(y) + abs(x) + abs(rot), 1.0)
                frontLeftPower = (rotY + rotX + rot) / denominator
                backLeftPower = (rotY - rotX + rot) / denominator
                frontRightPower = -(rotY - rotX - rot) / denominator
                backRightPower = -(rotY + rotX - rot) / denominator
            }
        }
        return MecanumOutput(frontLeftPower, backLeftPower, frontRightPower, backRightPower)
    }
}

enum class DrivetrainMapMode {
    BL_FL_BR_FR, FR_BR_FL_BL
}

data class MecanumOutput(
    val frontLeftPower: Double,
    val backLeftPower: Double,
    val frontRightPower: Double,
    val backRightPower: Double
)