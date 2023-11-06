package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.PixelGrabber;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * This tests the manual pixel grabber.
 * <p>
 * Features: {@link org.firstinspires.ftc.teamcode.features.PixelGrabber}
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
