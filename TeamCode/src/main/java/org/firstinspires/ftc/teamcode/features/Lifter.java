package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class Lifter extends Feature {
    @Override
    public void loop() {
        double motorSpeed = Devices.controller1.getLeftStickY();
        Devices.motor0.setSpeed(motorSpeed);
    }
}
