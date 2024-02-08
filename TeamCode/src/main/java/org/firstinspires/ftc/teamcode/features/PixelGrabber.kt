package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

/**
 * This controls the pixel grabber.
 *
 *
 * Connections: Uses the right servo in port 0, the left servo in port 1, and a controller.
 *
 *
 * Controls: Use the right bumper to close the grabber or the left bumper to open it.
 */
class PixelGrabber : Feature(), Buildable {
    var homePos0: Double = 70.0
    var homePos1: Double = 30.0
    var closePos0: Double = 62.5
    var closePos1: Double = 41.5

    override fun build() {
        manualClose()
    }

    override fun loop() {
        if (Devices.controller1.rightBumper) {
            manualClose()
        }
        if (Devices.controller1.leftBumper) {
            manualOpen()
        }
    }

    private fun manualOpen() {
        Devices.servo1.position = homePos1
        Devices.servo0.position = homePos0
    }

    private fun manualClose() {
        Devices.servo1.position = closePos1
        Devices.servo0.position = closePos0
    }
}
