package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.LinearSlide;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * This tests the linear slide.
 * <p>
 * Features: {@link org.firstinspires.ftc.teamcode.features.LinearSlide}
 */
public class LinearSlideTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new LinearSlide());
    }

    @Override
    public void run() {
        
    }
}
