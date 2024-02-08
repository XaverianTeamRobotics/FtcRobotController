package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This runs the intake system.
 *
 *
 * Connections: A motor in port 0 and a controller.
 *
 *
 * Controls: Press dpad up to turn on the intake.
 *
 *
 * @param <motorPower>
</motorPower> */
class Intake<motorPower> : OperationMode(), TeleOperation {
    override fun construct() {
    }

    override fun run() {
        val motorPower = motor0.power
        motor0.power = 100.0
        if (Devices.controller1.dpadUp) {
            if (motorPower == 100.0) {
                motor0.power = 100.0
            }
            if (motorPower < 100) {
                motor0.power = motorPower - 10
                if (motorPower <= 0) {
                    motor0.power = 0.0
                }
            }
        }
    }
}
