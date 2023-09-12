package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AnalogInput
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * An analog device is a device which records voltage passed through its port, like a voltage sensor intrgrated into the Control Hub itself. The OpMode is expected to interperet its readings as data returned by an analog sensor.
 */
class AnalogDevice(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeAnalogDeviceRequest(name)

    /**
     * The underlying analog device being managed by the jlooping request.
     */
    val device: AnalogInput
        get() = HardwareGetter.getAnalogDeviceFromRequest(name)

    /**
     * The current voltage being recorded by the device.
     */
    val voltage: Double
        get() = HardwareGetter.getAnalogDeviceData(name).voltage

    /**
     * The maximum voltage the device can output.
     */
    val maxVoltage: Double
        get() = HardwareGetter.getAnalogDeviceData(name).maxVoltage

}