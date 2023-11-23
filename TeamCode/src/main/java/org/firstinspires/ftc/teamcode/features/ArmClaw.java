package org.firstinspires.ftc.teamcode.features;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.motor0;
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
    private double counter1 = 0; //intake
    private double counter2 = 0; //left servo
    private double counter3 = 0; //right servo
    private boolean dpadPressed1 = false; //intake
    private boolean dpadPressed2 = false; //left servo
    private boolean dpadPressed3 = false; //right servo
    public void build(){
        servo0.setPosition(20); //lower the number for the servos to move closer and vice versa
        servo1.setPosition(80); //raise the number for the servos to move closer and vice versa
        servo2.setPosition(33);
        motor0.setPower(0);
    }
    /**
     *this method counts how many times Dpad Down has been pressed so that we can use it to turn the
     *intake on and off
     **/
    public int intakeCount() {
        if (Devices.controller2.getDpadDown() && !dpadPressed1) {
            counter1 += 1;
            dpadPressed1 = true;
        }
        else if (!Devices.controller2.getDpadDown()) {
            dpadPressed1 = false;
        }
        if (counter1 % 2 != 0) {
            return -100;
        }
        else {
            return 0;
        }
    }

    /**
     * this method counts how many times Dpad Left has been pressed so that we can use it to open
     * and close the left servo (left grabber)
     */
    public int leftButtonPressedCount() {
        if (Devices.controller2.getDpadLeft() && !dpadPressed2) {
            counter2 += 1;
            dpadPressed2 = true;
        }
        else if (!Devices.controller2.getDpadLeft()) {
            dpadPressed2 = false;
        }
        if (counter2 % 2 != 0) {
            return 5;
        }
        else {
            return 15;
        }
    }

    /**
     * this method counts how many times Dpad Right has been pressed so that we can use it to open
     * and close the right servo (right grabber)
     */
    public int rightButtonPressedCount() {
        if (Devices.controller2.getDpadRight() && !dpadPressed3) {
            counter3 += 1;
            dpadPressed3 = true;
        }
        else if (!Devices.controller2.getDpadRight()) {
            dpadPressed3 = false;
        }
        if (counter3 % 2 != 0) {
            return 100;
        }
        else {
            return 85;
        }
    }
    public void loop() {
        double motorPower = (Devices.controller2.getRightTrigger() - Devices.controller2.getLeftTrigger());
        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(motorPower);

        //the motors must move in opposite directions
        servo0.setPosition(leftButtonPressedCount()); //this operates the left grabber
        servo1.setPosition(rightButtonPressedCount()); //this operates the right grabber
        if (Devices.controller2.getLeftBumper()) {
            servo2.setPosition(33);
            //this will rotate the entire claw mechanism so that it is in line with the backboard
        }
        if (Devices.controller2.getRightBumper()) {
            servo2.setPosition(0);
            //this will return the claw mechanism back to the initial position
        }
        Devices.motor2.setPower(intakeCount());
    }

}
