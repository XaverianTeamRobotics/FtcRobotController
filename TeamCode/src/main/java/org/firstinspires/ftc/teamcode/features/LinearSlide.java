package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * Uses motor 4
 * Right trigger - raise
 * Left trigger - lower
 */
public class LinearSlide extends Feature {
    @Override
    public void loop() {
        Devices.motor4.setPower(-0.25 * (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()));
    }
}
