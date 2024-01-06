package org.firstinspires.ftc.teamcode.opmodes;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.features.ArmClaw;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class AutoArmClawTest extends OperationMode implements TeleOperation {
    ArmClaw claw;

    @Override
    public void construct() {
        claw = new ArmClaw();
        registerFeature(claw);
    }

    @Override
    public void run() {
        claw.autoRaiseArm(ArmClaw.KeyPositions.FOUR);
    }
}