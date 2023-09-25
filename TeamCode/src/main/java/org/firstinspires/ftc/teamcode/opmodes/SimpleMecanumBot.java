package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SimpleMecanumBot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(true));
    }

    @Override
    public void run() {

    }
}
