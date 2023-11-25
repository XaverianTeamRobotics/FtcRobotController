package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;//our two devices our the motors for the arm

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.*;

/**
 * this runs the grabber, intake, and claw pivot mechanism.  Dpad left is to close the left grabber,
 * Dpad right closes the right grabber, Dpad up opens both grabbers, Dpad down starts/stops the
 * intake, the bumpers rotate the claw mechanism, and the triggers move the arms.
 * <p>
 * All controls are in controller 2
 */
public class ArmClaw extends Feature implements Buildable {
    public static final int R_OPEN = 83, L_OPEN = 20;
    public static final int R_CLOSED = 100, L_CLOSED = 5;
    private double counter = 0;
    private boolean dpadPressed = false;
    private double counter2 = 0; //left servo
    private double counter3 = 0; //right servo
    private boolean dpadPressed2 = false; //left servo
    private boolean dpadPressed3 = false; //right servo
    public void build(){
        servo0.setPosition(L_OPEN);
        servo1.setPosition(R_OPEN);
        servo2.setPosition(0);
        motor0.setPower(0);
    }
    /**
     *this method counts how many times Dpad Down has been pressed so that we can use it to turn the
     *intake on and off
     **/
    private int intakeCount() {
        if (Devices.controller2.getDpadDown() && !dpadPressed) {
            counter += 1;
            dpadPressed = true;
        } else if (!Devices.controller2.getDpadDown()) dpadPressed = false;
        if (counter % 2 != 0) {
            return R_CLOSED;
        } else {
            return 0;
        }
    }

    /**
     * this method counts how many times Dpad Left has been pressed so that we can use it to open
     * and close the left servo (left grabber)
     */
    private int leftButtonPressedCount() {
        if (Devices.controller2.getDpadLeft() && !dpadPressed2) {
            counter2 += 1;
            dpadPressed2 = true;
        }
        else if (!Devices.controller2.getDpadLeft()) {
            dpadPressed2 = false;
        }
        if (counter2 % 2 != 0) {
            return L_CLOSED;
        }
        else {
            return L_OPEN;
        }
    }

    /**
     * this method counts how many times Dpad Right has been pressed so that we can use it to open
     * and close the right servo (right grabber)
     */
    private int rightButtonPressedCount() {
        if (Devices.controller2.getDpadRight() && !dpadPressed3) {
            counter3 += 1;
            dpadPressed3 = true;
        }
        else if (!Devices.controller2.getDpadRight()) {
            dpadPressed3 = false;
        }
        if (counter3 % 2 != 0) {
            return R_CLOSED;
        }
        else {
            return R_OPEN;
        }
    }

    public void loop() {
        double motorPower = -(Devices.controller2.getRightTrigger() - Devices.controller2.getLeftTrigger()) * 0.75;
        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(motorPower);

        //the motors must move in opposite directions
        servo0.setPosition(leftButtonPressedCount()); //this operates the left grabber
        servo1.setPosition(rightButtonPressedCount()); //this operates the right grabber
        if (Devices.controller2.getLeftBumper()) {
            servo2.setPosition(servo2.getPosition() + 0.5);
            //this will rotate the entire claw mechanism so that it is in line with the backboard
        }
        if (Devices.controller2.getRightBumper()) {
            servo2.setPosition(servo2.getPosition() - 0.5);
            //this will return the claw mechanism back to the initial position
        }
        Devices.motor2.setPower(-intakeCount());
    }

}
