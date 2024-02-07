package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * This controls the pixel grabber.
 * <p>
 * Connections: Uses the right servo in port 0, the left servo in port 1, and a controller.
 * <p>
 * Controls: Use the right bumper to close the grabber or the left bumper to open it.
 */
public class PixelGrabber extends Feature implements Buildable {

    double homePos0 = 70, homePos1 = 30.0;
    double closePos0 = 62.5, closePos1 = 41.5;

    @Override
    public void build() {
        manualClose();
    }

    @Override
    public void loop() {
        if (Devices.controller1.getRightBumper()) {
            manualClose();
        }
        if (Devices.controller1.getLeftBumper()) {
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
