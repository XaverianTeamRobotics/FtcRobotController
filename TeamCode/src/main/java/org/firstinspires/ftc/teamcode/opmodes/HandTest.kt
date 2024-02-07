package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
@Disabled
public class HandTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new Hand());
    }

    @Override
    public void run() {

    }

}
