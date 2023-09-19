package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class PixelGrabberTest extends OperationMode implements TeleOperation {

    double homePos0 = 0, homePos1 = 75;
    double closePos0 = 50, closePos1 = 66;

    @Override
    public void construct() {
        Devices.servo1.setPosition(homePos1);
        Devices.servo0.setPosition(homePos0);
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
        Devices.servo1.setPosition(homePos1);
        Devices.servo0.setPosition(homePos0);
    }

    private void manualClose() {
        Devices.servo1.setPosition(closePos1);
        Devices.servo0.setPosition(closePos0);
    }
}
