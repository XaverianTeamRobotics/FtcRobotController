package org.firstinspires.ftc.teamcode.opmodes;

import android.util.Log;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class Buttoner extends OperationMode implements TeleOperation {
    public void construct() {
        Devices.enableButtonRegistration();
        Devices.controller1.registerButton(GamepadRequestInput.A, "Testing");
    }

    public void run() {
        Object val = Devices.controller1.buttonSearch("Testing");
        Double val2 = (Double) val;
        Logging.log(val + "");
        //Devices.controller1.registerButton(GamepadRequestInput.A, "Testing"); // will fail
        Logging.log("Hi");

        

        //Devices.controller1.buttonSearch("Testing");
        //Devices.controller1.getRightTrigger(); // will fail the test
        Logging.update();
    }
}