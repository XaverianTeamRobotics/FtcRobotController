package org.firstinspires.ftc.teamcode.internals.settings

class EncoderConfig(name: String?, direction: OdoEncoder.Direction?) {
    @JvmField
    var NAME: String?
    @JvmField
    var DIRECTION: OdoEncoder.Direction?

    init {
        NAME = name
        DIRECTION = direction
    }

    override fun toString(): String {
        return NAME + " " + DIRECTION
    }
}
