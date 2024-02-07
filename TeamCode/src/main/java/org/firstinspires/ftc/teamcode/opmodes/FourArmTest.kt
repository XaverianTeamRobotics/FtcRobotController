package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.PowerplayFourMotorArm
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This tests the double reverse four-bar linkage system.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.PowerplayFourMotorArm]
 */
class FourArmTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(PowerplayFourMotorArm())
    }

    override fun run() {
    }
}
