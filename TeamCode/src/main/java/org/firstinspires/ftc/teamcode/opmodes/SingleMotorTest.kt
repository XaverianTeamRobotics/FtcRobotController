package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * Tests running a single motor0 with controller1's left and right trigger buttons.
 */
class SingleMotorTest : OperationMode(), TeleOperation {
    override fun construct() {
    }

    override fun run() {
        motor0.power = (Devices.controller1.rightTrigger - Devices.controller1.leftTrigger)
    }
}
