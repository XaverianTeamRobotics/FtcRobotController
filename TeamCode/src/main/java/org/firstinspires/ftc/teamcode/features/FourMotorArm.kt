package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller2
import org.firstinspires.ftc.teamcode.internals.motion.pid.basic.BasicPositionInputFilter
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging

class FourMotorArm: Feature(), Buildable {

    override fun build() {
        Devices.encoder5.reset()
        Devices.encoder6.reset() // Right side
        resumeManualControlInstantly()
    }

    companion object {
        // Used by the autonomous modes to prevent the driver from moving the arm
        // When false, manual control can be used
        @JvmStatic
        var autonomousOverride: Boolean = false

        // Used by the driver or internal logic to prevent the autonomous modes from moving the arm
        // Whenever the button is pressed or this function is called, this is set to true
        // But when the auto is inactave, this acts as a safety against bad code
        @JvmStatic
        var permitAutonomous: Boolean = false

        val basicPositionInputFilter = BasicPositionInputFilter(35.0, 75.0)

        @JvmStatic
        fun autoRunArm(height: Double) {
            basicPositionInputFilter.targetPosition = height
            autonomousOverride = true
            permitAutonomous = true
        }

        @JvmStatic
        fun autoRunArm(position: ArmPosition) {
            autoRunArm(position.height)
        }

        @JvmStatic
        fun autoComplete(): Boolean {
            return !permitAutonomous
        }

        @JvmStatic
        fun resumeManualControlInstantly() {
            permitAutonomous = false
            autonomousOverride = false
            Devices.motor4.power = 0.0
            Devices.motor5.power = 0.0
            Devices.motor6.power = 0.0
            Devices.motor7.power = 0.0
        }

        @JvmStatic
        fun autoLevelArm() {
            autoRunArm(Devices.encoder5.position.toDouble())
        }
    }

    enum class ArmPosition(val height: Double) {
        JNCT_GND    (209.0   ),
        JNCT_LOW    (840.0  ),
        JNCT_MED    (1410.0  ),
        JNCT_HIGH   (2000.0  ),
        JNCT_HIGH_LOWER   (1600.0  ),
        CONE_LOW    (90.0   ),
        CONE_MED    (300.0   ),
        CONE_HIGH   (425.0  ),
        RESET       (20.0    ),
    }

    override fun loop() {
        var powerL = 0.0
        var powerR = 0.0
        if (!autonomousOverride) {
            // Motor config: 4 - TL, 5 - BL, 6 - TR, 7 - BR
            val power = controller2.rightTrigger - (controller2.leftTrigger * 0.5)
            powerL = if (controller2.leftStickY > 25) - 75.0 else if (controller2.leftStickY < -25) + 75.0 else power
            powerR = if (controller2.rightStickY > 25) - 75.0 else if (controller2.rightStickY < -25) + 75.0 else power
        }
        else if (permitAutonomous) {
            powerL = basicPositionInputFilter.calculate(-Devices.encoder6.position.toDouble())
            powerR = basicPositionInputFilter.calculate(Devices.encoder5.position.toDouble())
            if (powerL == 0.0 && powerR == 0.0) {
                resumeManualControlInstantly()
            }
        }

        // Cap powerL and powerR and 100 and -100
        powerL = powerL.coerceIn(-100.0, 100.0)
        powerR = powerR.coerceIn(-100.0, 100.0)

        Devices.motor4.speed = powerL
        Devices.motor5.speed = -powerL
        Devices.motor6.speed = powerR
        Devices.motor7.speed = -powerR

        // Some gamepad binds
        if      (controller2.   triangle)  autoRunArm(ArmPosition.JNCT_HIGH)
        else if (controller2.   cross)     autoRunArm(ArmPosition.JNCT_GND)
        else if (controller2.   circle)    autoRunArm(ArmPosition.JNCT_MED)
        else if (controller2.   square)    autoRunArm(ArmPosition.JNCT_LOW)
        else if (controller2.   dpadLeft)  autoRunArm(ArmPosition.CONE_LOW) // 1 cone
        else if (controller2.   dpadRight) autoRunArm(ArmPosition.CONE_MED) // 3 cones
        else if (controller2.   dpadUp)    autoRunArm(ArmPosition.CONE_HIGH) // 5 cones
        else if (controller2.   dpadDown)  autoRunArm(ArmPosition.RESET)

        if (controller2.share || controller1.share) autoLevelArm()

        // manual override: any bumper will stop auto mode
        if (controller2.leftBumper || controller2.rightBumper || controller1.leftBumper || controller1.rightBumper) {
            resumeManualControlInstantly()
        }

        // Log the encoder values
        DSLogging.log("encoder2", Devices.encoder5.position)
        DSLogging.log("encoder1", -Devices.encoder6.position)
//        DSLogging.log("armDistanceSensor", Devices.armDistanceSensor.distance)
        DSLogging.update()
    }
}