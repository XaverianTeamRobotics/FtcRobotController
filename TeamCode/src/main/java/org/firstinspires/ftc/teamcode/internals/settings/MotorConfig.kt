package org.firstinspires.ftc.teamcode.internals.settings

import com.qualcomm.robotcore.hardware.DcMotorSimple

class MotorConfig(name: String?, direction: DcMotorSimple.Direction?) {
    @JvmField
    var NAME: String?
    @JvmField
    var DIRECTION: DcMotorSimple.Direction?

    init {
        NAME = name
        DIRECTION = direction
    }

    override fun toString(): String {
        return "$NAME $DIRECTION"
    }
}
