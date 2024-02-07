package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger;

import org.firstinspires.ftc.teamcode.internals.debug.Debugger;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        Logging.clear();
        RDWebSocketServer.enableMotorStatic(0, 3);
        RDWebSocketServer.enableServoStatic(0, 5);

        registerFeature(new Debugger());
    }

    @Override
    public void run() {

    }
}
