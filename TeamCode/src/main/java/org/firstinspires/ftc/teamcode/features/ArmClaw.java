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
    public static final int R_OPEN = 83, L_OPEN = 20;
    public static final int R_CLOSED = 100, L_CLOSED = 5;
    private double counter = 0;
    private boolean dpadPressed = false;
    public void build(){
        servo0.setPosition(L_OPEN);
        servo1.setPosition(R_OPEN);
        servo2.setPosition(0);
    }
    public int intakeCount() {
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
    public void loop() {
        double motorPower = -(Devices.controller2.getRightTrigger() - Devices.controller2.getLeftTrigger()) * 0.75;
        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(motorPower);

        //the motors must move in opposite directions
        if (Devices.controller2.getDpadLeft()) {
            servo0.setPosition(L_CLOSED);
            //closes the left grabber
        }
        if (Devices.controller2.getDpadRight()) {
            servo1.setPosition(R_CLOSED);
            //closes the right grabber
        }
        if (Devices.controller2.getDpadUp()) {
            servo0.setPosition(L_OPEN);
            servo1.setPosition(R_OPEN);
            //opens both grabbers
        }
        if (Devices.controller2.getSquare()) {
            servo0.setPosition(L_OPEN);
        }
        if (Devices.controller2.getCircle()) {
            servo1.setPosition(R_OPEN);
        }
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
