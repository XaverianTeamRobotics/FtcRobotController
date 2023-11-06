package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * This is a simple test for the servo controls.
 * <p>
 * Connections: A servo in port 2 and a controller.
 * <p>
 * Controls: Hold the square button to move down or the cross button to move up.
 */
public class ActuatorFeature extends Feature {

    @Override
    public void loop() {
        double total = Devices.servo2.getPosition();
        total -= Devices.controller1.getSquare() ? 1 : 0;
        total += Devices.controller1.getCross() ? 1 : 0;
        Devices.servo2.setPosition(total);
    }
}
