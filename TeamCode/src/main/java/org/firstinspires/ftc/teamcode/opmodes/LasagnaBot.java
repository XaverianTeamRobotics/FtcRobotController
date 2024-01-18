package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.*;
import org.firstinspires.ftc.teamcode.features.ArmClaw;
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage;
import org.firstinspires.ftc.teamcode.internals.documentation.ReferableButtonUsage;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

@ReferToButtonUsage(referTo = "AirplaneLauncher")
@ReferToButtonUsage(referTo = "ArmClaw")
public class LasagnaBot extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new AirplaneLauncher());
        registerFeature(new MecanumDrivetrain(false));
        registerFeature(new ArmClaw());
        registerFeature(new Lifter());
    }

    @Override
    public void run() {

    }
}
