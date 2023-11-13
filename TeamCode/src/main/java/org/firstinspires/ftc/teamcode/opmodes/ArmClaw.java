package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo0;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo1;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class ArmClaw extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        double motorPower = (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger());
        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(motorPower);
        //if (Devices.controller1.getDpadLeft()) {
            //servo0.setPosition(5);
            //closes the left grabber
        //}
        //if (Devices.controller1.getDpadRight()) {
            //servo1.setPosition(100);
            //closes the right grabber
        //}
        //if (Devices.controller1.getDpadUp()) {
            //servo0.setPosition(20);
            //servo1.setPosition(80);
            //opens both grabbers
        }
    }
//}
