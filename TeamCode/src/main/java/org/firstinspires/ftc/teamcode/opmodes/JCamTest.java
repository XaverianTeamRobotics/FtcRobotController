package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.JCam;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

public class JCamTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new JCam());
    }

    @Override
    public void run() {
        if(JCam.complete()) {
            DSLogging.log(JCam.down() ? "down" : "up");
            DSLogging.update();
            JCam.toggle();
        }
    }

}
