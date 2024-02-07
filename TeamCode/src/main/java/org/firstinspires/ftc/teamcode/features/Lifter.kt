package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

/**
 * This is a test the robot screw lifter.
 *
 *
 * Connections: A motor in port 0 and a controller.
 *
 *
 * Controls: Move the left stick up and down to control the motor.
 */
class Lifter : Feature() {
    override fun loop() {
        val motorSpeed = (Devices.controller1.rightTrigger - Devices.controller1.leftTrigger)
        Devices.motor3.speed = motorSpeed
    }
}
