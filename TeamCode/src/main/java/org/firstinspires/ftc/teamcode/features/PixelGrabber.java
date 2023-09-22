package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class PixelGrabber extends Feature implements Buildable {

    double homePos0 = 70, homePos1 = 33.3;
    double closePos0 = 60, closePos1 = 40;

    @Override
    public void build() {
        Devices.servo1.setPosition(homePos1);
        Devices.servo0.setPosition(homePos0);
    }

    @Override
    public void loop() {
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
