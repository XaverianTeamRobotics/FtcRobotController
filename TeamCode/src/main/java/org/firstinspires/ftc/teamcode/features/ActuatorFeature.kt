package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

/**
 * This is a simple test for the servo controls.
 *
 *
 * Connections: A servo in port 2 and a controller.
 *
 *
 * Controls: Hold the square button to move down or the cross button to move up.
 */
class ActuatorFeature : Feature() {
    override fun loop() {
        var total = Devices.servo2.position
        total -= (if (Devices.controller1.square) 1 else 0).toDouble()
        total += (if (Devices.controller1.cross) 1 else 0).toDouble()
        Devices.servo2.position = total
    }
}
