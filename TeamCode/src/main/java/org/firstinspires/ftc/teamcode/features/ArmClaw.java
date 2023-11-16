package org.firstinspires.ftc.teamcode.features;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.motor2;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.motor6;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo0;//this is the left grabber
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo1;//this is the right grabber
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.servo2;//this is the servo for the claw mechanism

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;//our two devices our the motors for the arm

/**
 * this runs the grabber, intake, and claw pivot mechanism.  Dpad left is to close the left grabber,
 * Dpad right closes the right grabber, Dpad up opens both grabbers, Dpad down starts/stops the
 * intake, the bumpers rotate the claw mechanism, and the triggers move the arms.
 * <p>
 * All controls are in controller 2
 */
public class ArmClaw extends Feature implements Buildable {
    public void build(){
        servo0.setPosition(20);
        servo1.setPosition(80);
        servo2.setPosition(0);
    }
    public int intakeCount() {
        motor6.setPower(0);
        double counter = 0;
        if (Devices.controller2.getDpadDown()) {
            counter += 1;
        }
        if (counter % 2 != 0) {
            return 100;
        }
        if (counter % 2 == 0) {
            return 0;
        } else {
            return 0;
        }
    }
    public void loop() {
        double motorPower = (Devices.controller2.getRightTrigger() - Devices.controller2.getLeftTrigger());
        Devices.motor4.setPower(motorPower);
        Devices.motor5.setPower(-motorPower);

        //the motors must move in opposite directions
        if (Devices.controller2.getDpadLeft()) {
            servo0.setPosition(5);
            //closes the left grabber
        }
        if (Devices.controller2.getDpadRight()) {
            servo1.setPosition(100);
            //closes the right grabber
        }
        if (Devices.controller2.getDpadUp()) {
            servo0.setPosition(20);
            servo1.setPosition(80);
            //opens both grabbers
        }
        if (Devices.controller2.getLeftBumper()) {
            servo2.setPosition(33);
            //this will rotate the entire claw mechanism so that it is in line with the backboard
        }
        if (Devices.controller2.getRightBumper()) {
            servo2.setPosition(0);
            //this will return the claw mechanism back to the initial position
        }
        Devices.motor6.setPower(intakeCount());
    }

}
