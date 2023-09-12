package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.ServoInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.ServoOptions

/**
 * A servo is similar to a motor but can only drive to a specific position.
 */
class Servo(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeServoRequest(name)

    /**
     * The underlying servo being managed by the jlooping request.
     */
    val servo: Servo
        get() {
            return HardwareGetter.getServoFromRequest(name)
        }

    /**
     * The position, from 0 to 100, to drive the servo to. This will be expanded into into a cartesian degree from 0 to 180 and then expanded or shrunk into the range of motion of the servo.
     */
    var position: Double = 0.0
        get() {
            val pos = HardwareGetter.issueServoRequest(name, ServoInput(0.0, ServoOptions.GET))
            field = pos
            return pos
        } set(value) {
            field = value
            HardwareGetter.issueServoRequest(name, ServoInput(value, ServoOptions.SET))
        }

}
