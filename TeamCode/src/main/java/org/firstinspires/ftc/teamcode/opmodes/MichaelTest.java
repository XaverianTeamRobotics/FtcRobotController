package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class MichaelTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        if (Devices.controller1.getA()) {
            Devices.getMotor0().setPower(100);
        } else {
            Devices.getMotor0().setPower(0);
        }
    }
}
