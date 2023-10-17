package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.PixelGrabber;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * Tests the manual pixel grabber.
 * Uses controller1's left and right bumpers to control servo0 and servo1.
 */
public class PixelGrabberTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new PixelGrabber());
    }

    @Override
    public void run() {

    }
}
