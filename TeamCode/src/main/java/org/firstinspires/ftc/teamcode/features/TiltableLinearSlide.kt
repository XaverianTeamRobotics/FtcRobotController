package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

/**
 * This controls a linear slide and an extra motor.
 *
 *
 * Connections: A motor connected to the linear slide in port 4 and a controller. Also uses motor in
 * port 5.
 *
 *
 * Controls: Use the right trigger to raise the slide and the left trigger to lower it. Move the
 * right stick up or down to control the motor in port 5.
 */
class TiltableLinearSlide : Feature() {
    override fun loop() {
        Devices.motor4.power = 0.25 * (Devices.controller1.rightTrigger - Devices.controller1.leftTrigger)
        Devices.motor5.power = -Devices.controller1.rightStickY
    }
}
