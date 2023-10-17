package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.LinearSlide;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * Test the linear slide feature.
 * Uses motor4 (controller1 trigger buttons) and motor5 (controller1 right stick y axis).
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
