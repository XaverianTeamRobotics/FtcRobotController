package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.misc.PoleCenterer
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class TunePoleCenterer : OperationMode(), TeleOperation {
    var centerer: PoleCenterer? = null

    override fun construct() {
        centerer = PoleCenterer()
        centerer!!.setDrivetrain(AutonomousDrivetrain())
    }

    override fun run() {
        centerer!!.center()
    }
}
