package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.*;
import org.firstinspires.ftc.teamcode.features.ArmClaw;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class LasagnaBot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrain(false));
        registerFeature(new ArmClaw());
    }

    @Override
    public void run() {

    }
}
