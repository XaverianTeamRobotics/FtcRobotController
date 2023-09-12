package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.GyroSensor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * A gyroscope measures its direction, somewhat like a compass.
 */
class Gyroscope(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeGyroscopeRequest(name)

    /**
     * The underlying gyroscope being managed by the jlooping request.
     */
    val gyroscope: GyroSensor
        get() {
            return HardwareGetter.getGyroscopeFromRequest(name)
        }

    /**
     * The heading of the Z-axis of the gyroscope in positive cartiesan degrees (0-360).
     */
    val heading: Int
        get() {
            return HardwareGetter.getGyroData(name).heading
        }

    /**
     * The raw orientation data returned by the gyroscope before being computed into a heading.
     */
    val raw: IntArray
        get() {
            return HardwareGetter.getGyroData(name).raw
        }

    /**
     * The operational status of the device as a human-readable string.
     */
    val status: String
        get() {
            return HardwareGetter.getGyroData(name).status
        }

}