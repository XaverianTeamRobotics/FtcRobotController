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

    private double total;
    private boolean squareP, crossP;

    @Override
    public void build() {
        total = 50;
        Devices.servo2.setPosition(total);
        squareP = false;
        crossP = false;
    }

    @Override
    public void loop() {
        if (!squareP) total -= Devices.controller1.getSquare() ? 5 : 0;
        if (!crossP) total += Devices.controller1.getCross() ? 5 : 0;
        squareP = Devices.controller1.getSquare();
        crossP = Devices.controller1.getCross();
        total = Math.min(Math.max(total, 50), 83.3);
        Devices.servo2.setPosition(total);
    }
}
