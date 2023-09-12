package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class PoleLocalizerTest extends OperationMode implements TeleOperation {
    PoleLocalizer navigator = new PoleLocalizer();
    @Override
    public void construct() {
        navigator.startStreaming();
    }

    @Override
    public void run() {
        Logging.log("X dist", navigator.getPoleDistanceX());
        Logging.log("Y dist", navigator.getPoleDistanceY());
        Logging.log("Dist", navigator.getPoleDistance());
        Logging.update();
    }
}
