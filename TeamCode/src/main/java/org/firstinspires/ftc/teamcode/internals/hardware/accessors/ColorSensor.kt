package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.Colors

/**
 * A color sensor can see sounds and hear colors. It is high on multiple psychedelic drugs.
 */
class ColorSensor(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeColorSensorRequest(name)

    /**
     * The underlying normalized color sensor being managed by the jlooping request. Most of the time you'll be using this over the raw variant.
     */
    val sensor: NormalizedColorSensor
        get() {
            return HardwareGetter.getColorSensorFromRequest(name).sanitized
        }

    /**
     * The underlying raw color sensor being managed by the jlooping request.
     */
    val rawSensor: ColorSensor
        get() {
            return HardwareGetter.getColorSensorFromRequest(name).raw
        }

    private val cdata: Colors
        get() {
            return HardwareGetter.getColorSensorData(name)
        }

    /**
     * The color read by the sensor in RGB format.
     */
    val rgb: IntArray
        get() {
            return cdata.rgba.copyOfRange(0, 3)
        }

    /**
     * The color read by the sensor in RGBA format.
     */
    val rgba: IntArray
        get() {
            return cdata.rgba
        }

    /**
     * The color read by the sensor in HSV format.
     */
    val hsv: DoubleArray
        get() {
            return cdata.hsv
        }

    /**
     * The color read by the sensor in grayscale.
     */
    val gsv: Int
        get() {
            return cdata.gsv
        }

    /**
     * The gain of the color read by the sensor.
     */
    val gain: Double
        get() {
            return cdata.gain
        }

    /**
     * The raw color read by the raw, non-normalized sensor in ARGB format. Returned as an unsigned integer meant to be bitshifted. Its digits are as follows: AARRGGBB.
     */
    val raw: Int
        get() {
            return cdata.raw
        }

}