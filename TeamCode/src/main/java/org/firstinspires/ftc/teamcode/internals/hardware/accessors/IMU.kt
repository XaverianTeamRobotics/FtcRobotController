package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.misc.Vector3

class IMU(override var name: String): DeviceAccessor(name) {

    private val request = HardwareGetter.makeIMURequest(name)

    val orientation: Vector3
        get() = HardwareGetter.getIMUData(name).orientation

    val acceleration: Vector3
        get() = HardwareGetter.getIMUData(name).acceleration
}