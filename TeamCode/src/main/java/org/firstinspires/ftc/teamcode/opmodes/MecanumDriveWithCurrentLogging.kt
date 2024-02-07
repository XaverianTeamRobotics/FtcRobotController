package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.*;
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class MecanumDriveWithCurrentLogging extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrain(false));
        registerFeature(new CurrentMonitoringFeature());
    }

    @Override
    public void run() {

    }
}
