package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class PoleNavigatorTest extends OperationMode implements TeleOperation {
    PoleNavigator navigator = new PoleNavigator();
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
