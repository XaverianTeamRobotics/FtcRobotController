package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.misc.PoleCenterer;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class TunePoleCenterer extends OperationMode implements TeleOperation {

    PoleCenterer centerer;

    @Override
    public void construct() {
        centerer = new PoleCenterer();
        centerer.setDrivetrain(new AutonomousDrivetrain());
    }

    @Override
    public void run() {
        centerer.center();
    }

}
