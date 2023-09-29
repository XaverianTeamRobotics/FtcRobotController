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
        total = 0;
        Devices.servo2.setPosition(0);
        squareP = false;
        crossP = false;
    }

    @Override
    public void loop() {
        if (!squareP) total -= Devices.controller1.getSquare() ? 10 : 0;
        if (!crossP) total += Devices.controller1.getCross() ? 10 : 0;
        squareP = Devices.controller1.getSquare();
        crossP = Devices.controller1.getCross();
        Devices.servo2.setPosition(total);
    }
}
