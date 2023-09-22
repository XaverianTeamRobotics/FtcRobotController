package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderPositionStore

/**
 * An encoder tracks the number of rotations of a hex rod and can be used to measure distance.
 */
class Encoder(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeEncoderRequest(name)

    /**
     * The underlying encoder managed as a DC motor being managed by the jlooping request.
     */
    val encoder: DcMotor
        get() {
            return HardwareGetter.getEncoderFromRequest(name)
        }

    /**
     * The current number of rotations, or position, of the encoder starting from 0. Forward rotations increase the number, while reverse ones decrease it. This value can be negative.
     */
    val position: Int
        get() {
            return HardwareGetter.issueEncoderRequest(name, EncoderInput.GET)
        }

    /**
     * Saves the current position of this encoder to the [EncoderPositionStore]. If this encoder is reinitialized without a robot reboot (for example, if the user changes OpModes), it will be set to this position on initialization. This position will override itself if this method is called again. This encoder's position will only be set to the saved position on initialization even if the saved position changes at runtime. This position will be disregarded after calling [reset].
     */
    fun save() {
        EncoderPositionStore.setPosition(name, position)
    }

    /**
     * Resets the encoder to 0.
     */
    fun reset() {
        HardwareGetter.issueEncoderRequest(name, EncoderInput.SET)
    }

}