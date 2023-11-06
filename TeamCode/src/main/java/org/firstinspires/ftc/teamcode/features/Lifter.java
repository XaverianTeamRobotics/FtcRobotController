package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * This is a test the robot screw lifter.
 * <p>
 * Connections: A motor in port 0 and a controller.
 * <p>
 * Controls: Move the left stick up and down to control the motor.
 */
public class Lifter extends Feature {
    @Override
    public void loop() {
        double motorSpeed = -Devices.controller1.getLeftStickY();
        Devices.motor0.setSpeed(motorSpeed);
    }
}
