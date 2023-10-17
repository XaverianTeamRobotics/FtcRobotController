package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ActuatorFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Loggers;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

/**
 * Test basic servo actuator functions.
 * Uses servo port 2.
 * Square = Down
 * Cross = Up
 */
public class ActuatorTest extends OperationMode implements TeleOperation {

    private double total;
    private boolean squareP, crossP;

    @Override
    public void construct() {
        registerFeature(new ActuatorFeature());
    }

    @Override
    public void run() {
    }
}
