package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.FourMotorArmNoManualLevel
import org.firstinspires.ftc.teamcode.features.Hand
import org.firstinspires.ftc.teamcode.features.PowerplayMecanumDrivetrain
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class SmallbotProductionStickDrift : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(PowerplayMecanumDrivetrain(false, true))
        registerFeature(FourMotorArmNoManualLevel())
        registerFeature(Hand())
    }

    override fun run() {}
}
