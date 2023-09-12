package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * A laser distance sensor is a distance sensor that shoots photons out and measures the time it takes for them to reflect back. It, obviously, uses a laser. Ultrasonic distance sensors are another FTC legal distance sensor option but are not supported by this class. Using a set of cameras to compute depth of field via parallax is the final FTC legal distance sensor option, but this is not supported by the 19460 API and EasyOpenCV, Tensorflow, or Vuforia must be used.
 */
class LaserDistanceSensor(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeLaserDistanceSensorRequest(name)

    /**
     * The underlying distance sensor being managed by the jlooping request.
     */
    val sensor: DistanceSensor
        get() {
            return HardwareGetter.getLaserDistanceSensorFromRequest(name)
        }

    /**
     * The unit to measure the distance with, such as inches or millimeters.
     */
    var unit: DistanceUnit = DistanceUnit.MM
        get() {
            return field
        } set(value) {
            field = value
        }

    /**
     * The distance recorded by the sensor. The default is millimeters.
     */
    val distance: Double
        get() {
            return HardwareGetter.issueLaserDistanceSensorRequest(name, unit)
        }

}