package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.*;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * grabberR = 0, grabberL = 1, grabberLift = 2, cam = 3, launcher = 4
 * slide = 4
 * slide rotation = 5
 * <p>
 * Square = Lower grabber
 * <p>
 * Cross = Raise grabber
 * <p>
 * Left stick - move
 * <p>
 * Right stick - turn
 * <p>
 * Right bumper - close grabber
 * <p>
 * Left bumper - open grabber
 * <p>
 * Right trigger - raise slide
 * <p>
 * Left trigger - lower slide
 * <p>
 * Dpad up - Launch plane
 * <p>
 * Dpad left/right - tilt arm
 */
public class Waterbot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(true));
        registerFeature(new PixelGrabber());
        registerFeature(new ActuatorFeature());
        registerFeature(new LinearSlide());
        registerFeature(new AirplaneLauncher());

    }

    @Override
    public void run() {

    }
}
