package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class PixelGrabberTest extends OperationMode implements TeleOperation {


    @Override
    public void construct() {
        Devices.servo1.setPosition(100);
        Devices.servo0.setPosition(0);
    }

    @Override
    public void run() {
        if (Devices.controller1.getLeftBumper()) {
            manualClose();
        }
        if (Devices.controller1.getRightBumper()) {
            manualOpen();
        }


    }

    private void manualOpen() {
        Devices.servo1.setPosition(100);
        Devices.servo0.setPosition(0);
    }

    private void manualClose() {
        Devices.servo1.setPosition(0);
        Devices.servo0.setPosition(100);
    }
}
