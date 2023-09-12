package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.ContinousServoInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.ContinousServoOptions

/**
 * A continous servo is a servo that can run in a direction forever but cannot drive to a specific position nor report its position data.
 */
class ContinousServo(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeContinousServoRequest(name)

    /**
     * The underlying continous servo being managed by the jlooping request.
     */
    val servo: CRServo
        get() {
            return HardwareGetter.getContinousServoFromRequest(name)
        }

    /**
     * The power of the servo between -100 and 100.
     */
    var power: Double = 0.0
        get() {
            return HardwareGetter.issueContinousServoRequest(name, ContinousServoInput(field, ContinousServoOptions.GET))
        } set(value) {
            field = value
            HardwareGetter.issueContinousServoRequest(name, ContinousServoInput(value, ContinousServoOptions.SET))
        }

}