package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo1;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class ServoHomeTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        servo1.setPosition(0);
    }
}
