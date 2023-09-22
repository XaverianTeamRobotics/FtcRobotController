package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class LinearSlide extends Feature {


    @Override
    public void loop() {
        Devices.motor0.setPower(-0.25 * (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()));
    }
}
