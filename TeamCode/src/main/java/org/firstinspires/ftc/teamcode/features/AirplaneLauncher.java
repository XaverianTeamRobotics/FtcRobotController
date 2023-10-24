package org.firstinspires.ftc.teamcode.features;


import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * Uses servo 4
 * Dpad up - launch
 */
public class AirplaneLauncher extends Feature implements Buildable {
    @Override
    public void build() {
        Devices.servo4.setPosition(0.0);
    }

    @Override
    public void loop() {
        if (Devices.controller1.getDpadUp()) Devices.servo4.setPosition(100.0);
    }
}
