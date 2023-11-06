package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * Tests running a single motor0 with controller1's left and right trigger buttons.
 */
public class SingleMotorTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        Devices.motor0.setPower((Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()));
    }
}
