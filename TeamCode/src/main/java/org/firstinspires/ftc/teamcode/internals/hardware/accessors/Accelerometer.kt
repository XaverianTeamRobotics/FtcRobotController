package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AccelerationSensor
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * An accelerometer measures acceleration.
 */
class Accelerometer(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeAccelerometerRequest(name)

    /**
     * The underlying accelerometer being managed by the jlooping request.
     */
    val accelerometer: AccelerationSensor
        get() = HardwareGetter.getAccelerometerFromRequest(name)

    /**
     * Gets the acceleration measured by this sensor.
     */
    val acceleration: Acceleration
        get() = HardwareGetter.getAccelerometerData(name).force

    /**
     * The operational status of the device as a human-readable string.
     */
    val status: String
        get() = HardwareGetter.getAccelerometerData(name).status

}