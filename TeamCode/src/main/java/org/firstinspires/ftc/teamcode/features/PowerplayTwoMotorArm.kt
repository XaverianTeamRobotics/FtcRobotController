package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

class PowerplayTwoMotorArm : Feature() {
    override fun loop() {
        val power0a0 = Devices.controller1.rightTrigger > 0
        val power1a0 = Devices.controller1.leftTrigger > 0
        val power0 = ((if (power0a0) 100 else 0) - (if (Devices.controller1.rightBumper) 100 else 0)).toDouble()
        val power1 = ((if (power1a0) 100 else 0) - (if (Devices.controller1.leftBumper) 100 else 0)).toDouble()
        Devices.motor4.speed = -power0
        Devices.motor5.speed = power1
    }
}
