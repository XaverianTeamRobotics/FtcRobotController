package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class DidSomeoneSayAirplanes extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        double hold;
        hold = 0;
        double release;
        release = 50;
        Devices.servo0.setPosition(hold);
        if (Devices.controller1.getCircle()) {
            Devices.servo0.setPosition(release);
            //circle button will release the airplane
        }
        if (Devices.controller1.getCross()) {
            Devices.servo0.setPosition(hold);
            //cross button will hold the elastic in place
        }
    }
}
