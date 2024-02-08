package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This tests the native mecanum drivetrain.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain]
 */
class SimpleMecanumBot : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(NativeMecanumDrivetrain(false))
    }

    override fun run() {
    }
}
