package org.firstinspires.ftc.teamcode.internals.motion.pid.basic;

/**
 * A class to drive a motor to a desired output
 * Recieves target position and current position as input, ouputs the required motor power between -100 and 100
 * It's goal is to drive the motor to a target position, not a velocity
 * Functions much more basic than PID, but is still useful for some applications
 * Often called a "bang-bang" controller, but I call it "basic" because it is way more basic than PID
 * As a result, it will only factor in:
 *  - The current position
 *  - The target position
 *  - The acceptable error
 */
public class BasicPositionInputFilter {
    private double targetPosition;
    private double currentPosition;
    private double acceptableError;
    private double power;

    /**
     * Creates a new BasicPositionInputFilter with the specified parameters.
     *
     * @param acceptableError The acceptable error for the motor/encoder.
     * @param power           The power to output to the motor. This value will be clamped between -100 and 100
     *                        It will also never change, as basic controllers simply output a constant power.
     *                        The side effect is for sensitive motors, a constant oscillation will occur
     */
    public BasicPositionInputFilter(double acceptableError, double power) {
        this(0.0, 0.0, acceptableError, power);
    }

    /**
     * Creates a new BasicPositionInputFilter with the specified parameters.
     * @param targetPosition The target position for the motor/encoder
     * @param currentPosition The current position of the motor/encoder
     * @param acceptableError The acceptable error for the motor/encoder.
     * @param power The power to output to the motor. This value will be clamped between -100 and 100
     *              It will also never change, as basic controllers simply output a constant power.
     *              The side effect is for sensitive motors, a constant oscillation will occur
    */
    public BasicPositionInputFilter(double targetPosition, double currentPosition, double acceptableError, double power) {
        this.targetPosition = targetPosition;
        this.currentPosition = currentPosition;
        this.acceptableError = acceptableError;
        this.power = power;
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(double currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getAcceptableError() {
        return acceptableError;
    }

    public void setAcceptableError(double acceptableError) {
        this.acceptableError = acceptableError;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double calculate() {
        // Calculate the error of the target and current positions and compare it to the acceptable error
        if (Math.abs((targetPosition - currentPosition)) < acceptableError) {
            return 0;
        } else if (targetPosition > currentPosition) {
            return power;
        } else {
            return -power;
        }
    }

    public double calculate(double currentPosition) {
        setTargetPosition(targetPosition);
        setCurrentPosition(currentPosition);
        return calculate();
    }

    public boolean atSetpoint() {
        return Math.abs((targetPosition - currentPosition) / targetPosition) < acceptableError;
    }

    public boolean atGoal() {
        return atSetpoint();
    }

    public double getPercentError() {
        return Math.abs((targetPosition - currentPosition) / targetPosition);
    }
}
