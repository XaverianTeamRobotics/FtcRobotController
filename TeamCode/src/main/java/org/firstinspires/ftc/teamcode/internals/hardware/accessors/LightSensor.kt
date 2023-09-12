package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.LightSensor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.RawBrightness

/**
 * A light sensor measures the brightness of light.
 */
class LightSensor(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeLightSensorRequest(name)

    /**
     * The underlying sensor being managed by the jlooping request.
     */
    val sensor: LightSensor
        get() {
            return HardwareGetter.getLightSensorFromRequest(name)
        }

    private var led = true

    /**
     * Enables the sensor's onboard LED if it exists.
     */
    fun enableLED() {
        led = true
        HardwareGetter.issueLightSensorRequest(name, true)
    }

    /**
     * Disables the sensor's onboard LED if it exists.
     */
    fun disableLED() {
        led = false
        HardwareGetter.issueLightSensorRequest(name, false)
    }

    /**
     * The brightness detected by the sensor.
     */
    val brightness: Double
        get() {
            return HardwareGetter.issueLightSensorRequest(name, led).brightness
        }

    /**
     * The raw brightness of the sensor and the maximum raw brightness the sensor can detect.
     */
    val raw: RawBrightness
        get() {
            val req = HardwareGetter.issueLightSensorRequest(name, led)
            return RawBrightness(req.rawBrightness, req.maxRawBrightness)
        }

    /**
     * The operational status of the device as a human-readable string.
     */
    val status: String
        get() {
            return HardwareGetter.issueLightSensorRequest(name, led).status
        }

}