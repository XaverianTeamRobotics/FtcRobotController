package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.PixelGrabber
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * This tests the manual pixel grabber.
 *
 *
 * Features: [org.firstinspires.ftc.teamcode.features.PixelGrabber]
 */
class PixelGrabberTest : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(PixelGrabber())
    }

    override fun run() {
    }
}
