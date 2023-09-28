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
    public void loop() {
        Devices.servo4.setPosition(0);
    }

    @Override
    public void build() {
        if (Devices.controller1.getDpadUp()) Devices.servo4.setPosition(100);
    }
}
