package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.Lifter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class LifterTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new Lifter());
    }

    @Override
    public void run() {

    }
}
