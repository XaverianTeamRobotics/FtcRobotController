package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger;

import org.firstinspires.ftc.teamcode.internals.debug.Debugger;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        RDWebSocketServer.enableMotorStatic(0);
        RDWebSocketServer.enableMotorStatic(1);
        RDWebSocketServer.enableMotorStatic(2);
        RDWebSocketServer.enableMotorStatic(3);
        registerFeature(new Debugger());
    }

    @Override
    public void run() {

    }
}
