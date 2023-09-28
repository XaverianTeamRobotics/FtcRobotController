package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ActuatorFeature;
import org.firstinspires.ftc.teamcode.features.LinearSlide;
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.features.PixelGrabber;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * grabberR = 0, grabberL = 1, grabberLift = 2, cam = 3, launcher = 4
 * slide = 4
 * slide rotation = 5
 *
 * Square = Lower grabber
 * Cross = Raise grabber
 * Left stick - move
 * Right stick - turn
 * Right bumper - close grabber
 * Left bumper - open grabber
 * Right trigger - raise slide
 * Left trigger - lower slide
 * Dpad up - Launch plane
 */
public class Waterbot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(true));
        registerFeature(new PixelGrabber());
        registerFeature(new ActuatorFeature());
        registerFeature(new LinearSlide());

    }

    @Override
    public void run() {

    }
}
