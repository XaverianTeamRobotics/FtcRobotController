package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.Lifter
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This tests the robot screw lifter.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.Lifter]
 */
class LifterTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(Lifter())
    }

    override fun run() {
    }
}
