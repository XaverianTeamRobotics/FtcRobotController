package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.AirplaneLauncher
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.Lifter
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

@ReferToButtonUsage(referTo = "AirplaneLauncher")
@ReferToButtonUsage(referTo = "ArmClaw")
class LasagnaBot : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(AirplaneLauncher())
        registerFeature(MecanumDrivetrain(false))
        registerFeature(ArmClaw())
        registerFeature(Lifter())
    }

    override fun run() {
    }
}
