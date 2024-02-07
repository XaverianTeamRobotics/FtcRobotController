package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.PowerplayFourMotorArm;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

/**
 * This tests the double reverse four-bar linkage system.
 * <p>
 * Features: {@link org.firstinspires.ftc.teamcode.features.PowerplayFourMotorArm}
 */
public class FourArmTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new PowerplayFourMotorArm());
    }

    @Override
    public void run() {

    }

}
