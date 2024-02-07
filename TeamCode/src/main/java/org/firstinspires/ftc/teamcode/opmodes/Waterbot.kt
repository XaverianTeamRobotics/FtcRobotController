package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.*
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

/**
 * grabberR = 0, grabberL = 1, grabberLift = 2, cam = 3, launcher = 4
 * slide = 4
 * slide rotation = 5
 *
 *
 * Square = Lower grabber
 *
 *
 * Cross = Raise grabber
 *
 *
 * Left stick - move
 *
 *
 * Right stick - turn
 *
 *
 * Right bumper - close grabber
 *
 *
 * Left bumper - open grabber
 *
 *
 * Right trigger - raise slide
 *
 *
 * Left trigger - lower slide
 *
 *
 * Dpad up - Launch plane
 *
 *
 * Right Stick Y - tilt arm
 */
class Waterbot : OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(NativeMecanumDrivetrain(DrivetrainMapMode.BL_FL_BR_FR, false, false, false))
        registerFeature(PixelGrabber())
        registerFeature(ActuatorFeature())
        registerFeature(TiltableLinearSlide())
        registerFeature(AirplaneLauncher())
    }

    override fun run() {
    }
}
