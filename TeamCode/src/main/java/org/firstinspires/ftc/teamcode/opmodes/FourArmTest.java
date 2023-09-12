package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class FourArmTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new FourMotorArm());
    }

    @Override
    public void run() {

    }

}
