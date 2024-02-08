package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import kotlin.math.max
import kotlin.math.min

/**
 * This tests the intake system.
 *
 *
 * Connections: A motor in port 0 and a controller.
 *
 *
 * Controls: Hold the right trigger to increase the intake speed, left trigger to decrese it, or
 * left bumper to stop it.
 */
class IntakeBeltTest : OperationMode(), TeleOperation {
    var intakeSpeed: Double = 0.0
    var intakeSensitivity: Double = .0002
    override fun construct() {
    }

    override fun run() {
        intakeSpeed += intakeSensitivity * Devices.controller1.rightTrigger
        intakeSpeed -= intakeSensitivity * Devices.controller1.leftTrigger
        if (Devices.controller1.leftBumper) {
            intakeSpeed = 0.0
        }
        intakeSpeed = min(100.0, intakeSpeed)
        intakeSpeed = max(-100.0, intakeSpeed)
        motor0.power = intakeSpeed
        Logging.log("Intake Speed", intakeSpeed)
        Logging.update()
    }
}
