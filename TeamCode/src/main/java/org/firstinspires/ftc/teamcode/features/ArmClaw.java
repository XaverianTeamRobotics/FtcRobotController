package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;//our two devices our the motors for the arm
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.*;

/**
 * this runs the grabber, intake, and claw pivot mechanism.  Dpad left is to close the left grabber,
 * Dpad right closes the right grabber, Dpad up opens both grabbers, Dpad down starts/stops the
 * intake, the bumpers rotate the claw mechanism, and the triggers move the arms.
 * <p>
 * All controls are in controller 2
 * <p>
 * The SHARE/BACK button resets the home position of the arm
 * <p>
 * The OPTIONS/START button disables auto claw rotation and the auto lift
 */
public class ArmClaw extends Feature implements Buildable {
    public static final int R_OPEN = 10, L_OPEN = 75;
    public static final int R_CLOSED = 0, L_CLOSED = 60;
    private double counter = 0;
    private boolean dpadPressed = false;
    private double counterLeft = 0; //left servo
    private double counterRight = 0; //right servo
    private boolean dpadPressedLeft = false; //left servo
    private boolean dpadPressedRight = false; //right servo
    private int encoderHomePosition = 0;
    private boolean autonomousArmControl = false;
    private boolean autonomousIntakeControl = false;
    private boolean autonomousClawRotationControl = false;
    private boolean autonomousClawReleaseControl = false;
    private int armTargetHeight;
    private boolean armLiftingInProgress = false;

    public void build(){
        servo3.setPosition(L_OPEN);
        servo2.setPosition(R_OPEN);
        servoPickupPos();
        motor0.setPower(0);
        encoderHomePosition = -encoder3.getPosition();
        setHumanArmControl();
        setHumanIntakeControl();
        setHumanClawRotationControl();
        setHumanClawReleaseControl();
    }

    /**
     * This method contains five key positions for the arm. Home represents the initial position, with the arm stowed.
     * One through five represent five different positions for the arm to be in.
     * The ONE position is also the position used as the threshold for the auto claw rotation.
     */
    public enum KeyPositions {
        HOME(0),
        ONE(500), // temp
        TWO(750), // temp
        THREE(1000), // temp
        FOUR(1500), // temp
        FIVE(2000); // temp

        private final int position;

		KeyPositions(int position) {
			this.position = position;
		}

        public int getPosition() {
            return position;
        }
	}

    public enum ControlMode {
        HUMAN,
        AUTONOMOUS
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
            return 100;
        } else {
            return 0;
        }
    }

    /**
     * this method counts how many times Dpad Left has been pressed so that we can use it to open
     * and close the left servo (left grabber)
     */
    private int leftButtonPressedCount() {
        if (Devices.controller2.getDpadRight() && !dpadPressedLeft) {
            counterLeft += 1;
            dpadPressedLeft = true;
        }
        else if (!Devices.controller2.getDpadRight()) {
            dpadPressedLeft = false;
        }
        if (counterLeft % 2 != 0) {
            return L_CLOSED;
        }
        else {
            return L_OPEN;
        }
    }

    public void openLeftGrabber() {
        servo3.setPosition(L_OPEN);
    }

    public void closeLeftGrabber() {
        servo3.setPosition(L_CLOSED);
    }

    /**
     * this method counts how many times Dpad Right has been pressed so that we can use it to open
     * and close the right servo (right grabber)
     */
    private int rightButtonPressedCount() {
        if (Devices.controller2.getDpadLeft() && !dpadPressedRight) {
            counterRight += 1;
            dpadPressedRight = true;
        }
        else if (!Devices.controller2.getDpadLeft()) {
            dpadPressedRight = false;
        }
        if (counterRight % 2 != 0) {
            return R_CLOSED;
        }
        else {
            return R_OPEN;
        }
    }

    public void openRightGrabber() {
        servo2.setPosition(R_OPEN);
    }

    public void closeRightGrabber() {
        servo2.setPosition(R_CLOSED);
    }

    public void autoStartIntake() {
        blockHumanIntakeControl();
        motor2.setPower(-100);
    }

    public void autoStopIntake() {
        blockHumanIntakeControl();
        motor2.setPower(0);
    }

    public void setHumanIntakeControl() {
        autonomousIntakeControl = false;
        motor2.setPower(-intakeCount());
    }

    public void blockHumanIntakeControl() {
        autonomousIntakeControl = true;
    }

    public void setHumanClawRotationControl() {
        autonomousClawRotationControl = false;
    }

    public void blockHumanClawRotationControl() {
        autonomousClawRotationControl = true;
    }

    public void autoRotateClaw1(int i) {
        blockHumanClawRotationControl();
        servo0.setPosition(i);
    }

    public void autoRotateClaw2(int i) {
        blockHumanClawRotationControl();
        servo1.setPosition(i);
    }

    public void autoRaiseArm(KeyPositions pos) {
        autoRaiseArm(pos.getPosition());
    }

    public void autoRaiseArm(int height) {
        blockHumanArmControl();
        armTargetHeight = height;
        armLiftingInProgress = true;
    }

    public void setHumanArmControl() {
        autonomousArmControl = false;
    }

    public void blockHumanArmControl() {
        autonomousArmControl = true;
    }

    public void setHumanClawReleaseControl() {
        autonomousClawReleaseControl = false;
    }

    public void blockHumanClawReleaseControl() {
        autonomousClawReleaseControl = true;
    }

    public void armEmergencyStop() {
        Devices.motor0.setPower(0);
        Devices.motor1.setPower(0);
    }

    public ControlMode getArmControlMode() {
        if (autonomousArmControl) {
            return ControlMode.AUTONOMOUS;
        } else {
            return ControlMode.HUMAN;
        }
    }

    public ControlMode getIntakeControlMode() {
        if (autonomousIntakeControl) {
            return ControlMode.AUTONOMOUS;
        } else {
            return ControlMode.HUMAN;
        }
    }

    public ControlMode getClawRotationControlMode() {
        if (autonomousClawRotationControl) {
            return ControlMode.AUTONOMOUS;
        } else {
            return ControlMode.HUMAN;
        }
    }

    public ControlMode getClawReleaseControlMode() {
        if (autonomousClawReleaseControl) {
            return ControlMode.AUTONOMOUS;
        } else {
            return ControlMode.HUMAN;
        }
    }

    public boolean isArmLiftingInProgress() {
		return autonomousArmControl && armLiftingInProgress;
    }

    public double getArmDistanceSensor() {
        return distanceSensor.getDistance();
    }

    public void servoPickupPos() {
        servo0.setPosition(20);
        servo1.setPosition(5);
    }

    public void loop() {
        if (Devices.controller2.getStart()) {
            setHumanArmControl();
            armEmergencyStop();
            setHumanIntakeControl();
            setHumanClawRotationControl();
            setHumanClawReleaseControl();
        }

        Logging.log("Arm Control Mode", getArmControlMode());
        Logging.log("Intake Control Mode", getIntakeControlMode());
        Logging.log("Claw Rotation Control Mode", getClawRotationControlMode());
        Logging.log("Claw Release Control Mode", getClawReleaseControlMode());
        Logging.log("Arm Encoder Position", -encoder3.getPosition());
        Logging.log("Servo0 pos", servo0.getPosition());
        Logging.log("Servo1 pos", servo1.getPosition());
        Logging.update();

        double motorPower;
        if (!autonomousArmControl) {
            motorPower = -(Devices.controller2.getRightTrigger() - Devices.controller2.getLeftTrigger()) * 0.75;

            if (controller1.getBack()) {
                encoderHomePosition = -encoder3.getPosition();
            }
        } else {
            double deltaPos = armTargetHeight + encoder3.getPosition();
            if (deltaPos < -25) motorPower = 100;
            else if (deltaPos > 25) motorPower = -100;
            else {
                armLiftingInProgress = false;
                motorPower = 0;
                setHumanArmControl();
            }
        }

        Devices.motor0.setPower(motorPower);
        Devices.motor1.setPower(motorPower);

        if (!autonomousClawReleaseControl) {
            //the motors must move in opposite directions
            servo3.setPosition(leftButtonPressedCount()); //this operates the left grabber
            servo2.setPosition(rightButtonPressedCount()); //this operates the right grabber
        }

        if (!autonomousClawRotationControl) {
            if (controller2.getTriangle()) {
                servoPickupPos();
            } else {
                double grabber0Pos = servo0.getPosition();
                grabber0Pos += controller2.getLeftStickX() * 0.0005;
                grabber0Pos = Math.max(0, Math.min(100, grabber0Pos));
                servo0.setPosition(grabber0Pos);

                double grabber1Pos = servo1.getPosition();
                grabber1Pos += controller2.getRightStickX() * 0.0005;
                grabber1Pos = Math.max(0, Math.min(100, grabber1Pos));
                servo1.setPosition(grabber1Pos);
            }
        }

        if (!autonomousIntakeControl) {
            Devices.motor2.setPower(-intakeCount());
            if (controller2.getDpadUp()) motor2.setPower(100);
            if (controller2.getLeftBumper()) {
                servo5.setPosition(100);
            }
            else if (controller2.getRightBumper()) {
                servo5.setPosition(0);
            }
            if (controller2.getSquare()) servo4.setPosition(100);
            else if (controller2.getCircle()) servo4.setPosition(40);
        }
    }
}
