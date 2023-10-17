package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.Lifter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * Test the robot screw lifter feature.
 * Uses controller1 and motor0.
 */
public class LifterTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new Lifter());
    }

    @Override
    public void run() {

    }
}
