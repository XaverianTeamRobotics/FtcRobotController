package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class LinearSlideTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        Devices.motor0.setPower(-0.25 * (Devices.controller1.getRightTrigger()-Devices.controller1.getLeftTrigger()));
    }
}
