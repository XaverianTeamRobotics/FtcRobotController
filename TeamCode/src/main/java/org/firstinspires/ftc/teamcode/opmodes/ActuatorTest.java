package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Loggers;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class ActuatorTest extends OperationMode implements TeleOperation {

    private double total;
    private boolean squareP, crossP;

    @Override
    public void construct() {
        total = 0;
        Devices.servo0.setPosition(0);
        squareP = false;
        crossP = false;
    }

    @Override
    public void run() {
        if (!squareP) total -= Devices.controller1.getSquare() ? 10 : 0;
        if (!crossP) total += Devices.controller1.getCross() ? 10 : 0;
        squareP = Devices.controller1.getSquare();
        crossP = Devices.controller1.getCross();
        Logging.log("Total", total);
        Logging.update();
        Devices.servo0.setPosition(total);
    }
}
