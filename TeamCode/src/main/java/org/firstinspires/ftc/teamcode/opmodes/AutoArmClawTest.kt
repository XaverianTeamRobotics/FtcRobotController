package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class AutoArmClawTest : OperationMode(), TeleOperation {
    var claw: ArmClaw? = null

    override fun construct() {
        claw = ArmClaw()
        registerFeature(claw!!)
    }

    override fun run() {
        claw!!.autoRaiseArm(ArmClaw.KeyPositions.FOUR)
    }
}