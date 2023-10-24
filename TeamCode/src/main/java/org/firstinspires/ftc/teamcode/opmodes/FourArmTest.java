package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * Uses motors 4-7 and encoders 5-6
 * Use controller2's triggers to control the arm.
 * Use controller2's sticks to control each side of the arm independently
 */
public class FourArmTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new FourMotorArm());
    }

    @Override
    public void run() {

    }

}
