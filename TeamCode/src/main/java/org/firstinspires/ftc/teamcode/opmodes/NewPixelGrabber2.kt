package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo0;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo1;

import android.bluetooth.BluetoothClass;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class NewPixelGrabber2 extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        servo0.setPosition(20);
        servo1.setPosition(80);

    }

    @Override
    public void run() {
        if (Devices.controller1.getDpadLeft()) {
            servo0.setPosition(5);
        }
        if (Devices.controller1.getDpadRight()) {
            servo1.setPosition(100);
        }
        if (Devices.controller1.getDpadUp()) {
            servo0.setPosition(20);
            servo1.setPosition(80);
        }
    }
}
