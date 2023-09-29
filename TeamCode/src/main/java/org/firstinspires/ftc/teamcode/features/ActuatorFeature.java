package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

/**
 * Uses servo port 2.
 * Square = Down
 * Cross = Up
 */
public class ActuatorFeature extends Feature implements Buildable {

    @Override
    public void build() {
        Devices.servo2.setPosition(0);
    }

    @Override
    public void loop() {
        if (Devices.controller1.getSquare()) Devices.servo2.setPosition(100);
        else if (Devices.controller1.getCross()) Devices.servo2.setPosition(0);
    }
}
