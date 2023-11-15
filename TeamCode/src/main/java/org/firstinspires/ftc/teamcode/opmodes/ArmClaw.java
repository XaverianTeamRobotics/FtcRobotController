package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo0;//this is the left grabber
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo1;//this is the right grabber
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo2;//this is the servo for the claw mechanism

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;//our two devices our the motors for the arm
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class ArmClaw extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        servo0.setPosition(20);
        servo1.setPosition(80);
        servo2.setPosition(0);
    }

    @Override
    public void run() {
        double motorPower = (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger());
        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(-motorPower);

        //the motors must move in opposite directions
        if (Devices.controller1.getDpadLeft()) {
            servo0.setPosition(5);
            //closes the left grabber
        }
        if (Devices.controller1.getDpadRight()) {
            servo1.setPosition(100);
            //closes the right grabber
        }
        if (Devices.controller1.getDpadUp()) {
            servo0.setPosition(20);
            servo1.setPosition(80);
            //opens both grabbers
        }
        if (Devices.controller1.getLeftBumper()) {
            servo2.setPosition(30);
            //this will rotate the entire claw mechanism so that it is in line with the backboard
        }
        if (Devices.controller1.getRightBumper()) {
            servo2.setPosition(0);
            //this will return the claw mechanism back to the initial position
        }
    }
}
