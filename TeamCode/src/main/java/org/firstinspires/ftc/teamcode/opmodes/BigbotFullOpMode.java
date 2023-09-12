package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.features.TwoMotorArm;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
@Disabled
public class BigbotFullOpMode extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(
            DrivetrainMapMode.FR_BR_FL_BL,
            false, true,
            false, false));
        registerFeature(new TwoMotorArm());
        registerFeature(new Hand());
    }

    @Override
    public void run() {

    }

}
