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
public class ActuatorFeature extends Feature{

    @Override
    public void loop() {
        double total = Devices.servo2.getPosition();
        total -= Devices.controller1.getSquare() ? 1 : 0;
        total += Devices.controller1.getCross() ? 1 : 0;
        Devices.servo2.setPosition(total);
    }
}
