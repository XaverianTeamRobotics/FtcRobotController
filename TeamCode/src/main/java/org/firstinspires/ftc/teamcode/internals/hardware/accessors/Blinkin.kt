package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinOptions

/**
 * A Blinkin manages a Blinkin-compatible LED strip.
 */
class Blinkin(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeBlinkinRequest(name)

    /**
     * The underlying Blinkin servo being managed by the jlooping request.
     */
    val driver: RevBlinkinLedDriver
        get() = HardwareGetter.getBlinkinFromRequest(name)

    /**
     * The ID of the color to display in the strip. Valid IDs are numbers to the between 0.2525 and 0.7475.
     */
    var id: RevBlinkinLedDriver.BlinkinPattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE // rgb makes it go fast like gaming pc. vroom
        get() {
            return HardwareGetter.issueBlinkinRequest(name, BlinkinInput(field, BlinkinOptions.GET))
        } set(value) {
            HardwareGetter.issueBlinkinRequest(name, BlinkinInput(value, BlinkinOptions.SET))
            field = value
        }

}