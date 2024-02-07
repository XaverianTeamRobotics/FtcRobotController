package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.TiltableLinearSlide;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * This tests the linear slide. Uses a motor on motor 0, and the controls are the left and right triggers.
 */
public class LinearSlideTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        Devices.motor0.setPower(Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger());
    }
}
