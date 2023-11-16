package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.*;
import org.firstinspires.ftc.teamcode.features.ArmClaw;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

import kotlin.properties.ObservableProperty;

public class ScrimmageBotCenterstage1 extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(true));
        registerFeature(new ArmClaw());
        registerFeature(new AirplaneLauncher());
    }

    @Override
    public void run() {

    }
}
