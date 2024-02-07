package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * This controls a linear slide and an extra motor.
 * <p>
 * Connections: A motor connected to the linear slide in port 4 and a controller. Also uses motor in
 * port 5.
 * <p>
 * Controls: Use the right trigger to raise the slide and the left trigger to lower it. Move the
 * right stick up or down to control the motor in port 5.
 */
public class TiltableLinearSlide extends Feature {
    @Override
    public void loop() {
        Devices.motor4.setPower(0.25 * (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()));
        Devices.motor5.setPower(-Devices.controller1.getRightStickY());
    }
}
