package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.CurrentMonitoringFeature
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class MecanumDriveWithCurrentLogging : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(MecanumDrivetrain(false))
        registerFeature(CurrentMonitoringFeature())
    }

    override fun run() {
    }
}
