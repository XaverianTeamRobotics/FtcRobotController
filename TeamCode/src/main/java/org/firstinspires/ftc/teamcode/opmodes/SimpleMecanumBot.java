package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * This tests the native mecanum drivetrain.
 * <p>
 * Features: {@link org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain}
 */
public class SimpleMecanumBot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(false));
    }

    @Override
    public void run() {

    }
}
