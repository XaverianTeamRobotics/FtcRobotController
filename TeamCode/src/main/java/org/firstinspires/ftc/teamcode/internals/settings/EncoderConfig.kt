package org.firstinspires.ftc.teamcode.internals.settings

class EncoderConfig(name: String?, direction: OdoEncoder.Direction?) {
    @JvmField
    var NAME: String? = name

    @JvmField
    var DIRECTION: OdoEncoder.Direction? = direction

    override fun toString(): String {
        return NAME + " " + DIRECTION
    }
}
