package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class DidSomeoneSayAirplanes extends Feature {
    @Override
    public void loop() {
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
