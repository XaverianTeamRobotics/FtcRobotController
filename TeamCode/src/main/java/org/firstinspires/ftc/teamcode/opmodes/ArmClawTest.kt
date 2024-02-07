package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class ArmClawTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(ArmClaw())
    }

    override fun run() {
    }
}