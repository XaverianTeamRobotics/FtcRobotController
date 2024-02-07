package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo0
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo1
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class NewPixelGrabber2 : OperationMode(), TeleOperation {
    override fun construct() {
        servo0.position = 20.0
        servo1.position = 80.0
    }

    override fun run() {
        if (Devices.controller1.dpadLeft) {
            servo0.position = 5.0
        }
        if (Devices.controller1.dpadRight) {
            servo1.position = 100.0
        }
        if (Devices.controller1.dpadUp) {
            servo0.position = 20.0
            servo1.position = 80.0
        }
    }
}
