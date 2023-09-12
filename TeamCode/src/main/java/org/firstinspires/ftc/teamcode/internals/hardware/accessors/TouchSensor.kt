package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * A touch sensor can detect touch.
 */
class TouchSensor(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeTouchSensorRequest(name)

    /**
     * The underlying sensor being managed by the jlooping request.
     */
    val sensor: TouchSensor
        get() {
            return HardwareGetter.getTouchSensorFromRequest(name)
        }

    /**
     * Whether the sensor is detecting being touched or not.
     */
    fun isPressed(): Boolean {
        return HardwareGetter.getTouchSensorData(name).pressed
    }

    /**
     * The force of the touch. Some sensors can detect how intense a touch is, from a light tap to a strong press on the sensor. Sensors which don't support this feature will return an integer representation of a boolean denoting whether it's touched or not.
     */
    val force: Double
        get() {
            return HardwareGetter.getTouchSensorData(name).force
        }

}
