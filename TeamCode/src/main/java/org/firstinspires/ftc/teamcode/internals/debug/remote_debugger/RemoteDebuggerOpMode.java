package org.firstinspires.ftc.teamcode.internals.debug.remote_debugger;

import org.firstinspires.ftc.teamcode.internals.debug.Debugger;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new Debugger());
    }

    @Override
    public void run() {

    }
}
