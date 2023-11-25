package org.firstinspires.ftc.teamcode.features;


import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * This sets up the airplane launcher servo and uses it to launch the plane.
 * <p>
 * Connections: A servo in port 4 and a controller.
 * <p>
 * Controls: Press triangle to launch the plane.
 */
public class AirplaneLauncher extends Feature implements Buildable {
    @Override
    public void build() {
        Devices.servo3.setPosition(50.0);
    }

    @Override
    public void loop() {
        if (Devices.controller1.getTriangle()) Devices.servo3.setPosition(100.0);
    }
}
