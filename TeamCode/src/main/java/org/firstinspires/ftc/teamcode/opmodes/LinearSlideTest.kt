package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This tests the linear slide. Uses a motor on motor 0, and the controls are the left and right triggers.
 */
class LinearSlideTest : OperationMode(), TeleOperation {
    override fun construct() {
    }

    override fun run() {
        motor0.power = Devices.controller1.rightTrigger - Devices.controller1.leftTrigger
    }
}
