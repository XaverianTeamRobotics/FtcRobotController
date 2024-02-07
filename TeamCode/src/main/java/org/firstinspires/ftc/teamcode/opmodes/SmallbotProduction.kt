package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.PowerplayFourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.PowerplayMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SmallbotProduction extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new PowerplayMecanumDrivetrain(false, true));
        registerFeature(new PowerplayFourMotorArm());
        registerFeature(new Hand());
    }

    @Override
    public void run() {}

}
