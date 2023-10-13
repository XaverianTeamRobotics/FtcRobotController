package org.firstinspires.ftc.teamcode.internals.image;

import kotlin.math.MathKt;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

/**
 * Translate the angles from the camera to robot center.
 * (Note: Units are in DEGREES)
 */
public class CameraTranslation {
    private double offsetX = 0, offsetY = 0;
    private boolean cameraSwivel = false;
    private double cameraMinAngle = -135;
    private double cameraMaxAngle = 135;

    public CameraTranslation(double offsetX, double offsetY, boolean cameraSwivel, double cameraMinAngle, double cameraMaxAngle) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.cameraSwivel = cameraSwivel;
        this.cameraMinAngle = cameraMinAngle;
        this.cameraMaxAngle = cameraMaxAngle;
    }

    public CameraTranslation(double offsetX, double offsetY, boolean cameraSwivel) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.cameraSwivel = cameraSwivel;
    }

    public CameraTranslation(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public CameraTranslation() {}

    /**
     * Convert the getPosition() value of the servo to the angle of the camera.
     * @param servoAngle A value between -100 and 100. Output of servoX.getPosition()
     * @return The angle of the camera relative to the front of the robot. (In degrees)
     */
    public double convertServoAngleToCameraAngle(double servoAngle) {
        return ((27*servoAngle) / 10) - 135;
    }

    /**
     * Convert an angle to the required servo rotation.
     * If the angle is out of the bounds of the cameraMinAngle and cameraMaxAngle, the servo will be set to the closest bound.
     * @param angle The desired angle to set the camera to point to. (In degrees)
     * @return A value between -100 and 100. Input to servoX.setPosition()
     */
    public double convertAngleToServoAngle(double angle) {
        return ((10*angle)/27) + 50;
    }

    /**
     * Convert the angle of the servo and the angle from camera to target to the angle from the robot to the target.
     * (NOTE: PRIVATE TO PREVENT CONFUSION WITH convertCameraBearingAndRangeToRobotCentric)
     * @param servoAngle A value between -100 and 100. Output of servoX.getPosition()
     * @param angleToTarget The angle from the camera to the target. (In degrees)
     * @return The angle from the robot to the target. (In degrees)
     */
    private double convertCameraAngleToRobotCentric(double servoAngle, double angleToTarget) {
        return convertServoAngleToCameraAngle(servoAngle) + angleToTarget;
    }

    /**
     * Convert the target's bearing and range relative to the camera to be relative to the robot.
     * Accounts for the offset of the camera from the center of the robot.
     * @param servoAngle A value between -100 and 100. Output of servoX.getPosition()
     * @param bearing The bearing of the target relative to the camera. (In degrees)
     * @param range The range of the target relative to the camera. (In inches)
     * @return A double array containing the bearing and range of the target relative to the robot. (In degrees and inches)
     *       Index 0 is the bearing, index 1 is the range.
     */
    public double[] convertCameraBearingAndRangeToRobotCentric(double servoAngle, double bearing, double range) {
        double angle = convertCameraAngleToRobotCentric(servoAngle, bearing);
        // Draw a triangle from the camera to the target.
        // The angle of the hypotenuse is the sum of the angle from the robot to the camera and the angle from the camera to the target.
        // The length of the hypotenuse is the range of the target relative to the camera.
        angle = toRadians(angle);
        double x = Math.cos(angle) * range;
        double y = Math.sin(angle) * range;
        x += offsetX;
        y += offsetY;
        // Convert x and y to polar coordinates.
        double newBearing = Math.atan2(y, x);
        double newRange = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return new double[] {toDegrees(newBearing), newRange};
    }

    /**
     * Get the input to servoX.setPosition() to keep the camera's target in the center of its FOV
     * @param servoAngle The current angle of the servo. Between -100 and 100. Output of servoX.getPosition()
     * @param bearing The bearing of the target relative to the camera. (In degrees)
     * @return The input to servoX.setPosition() to keep the target in the center of the camera's FOV.
     */
    public double centerCameraInServo(double servoAngle, double bearing) {
        return Math.min(Math.max(convertAngleToServoAngle(convertCameraAngleToRobotCentric(servoAngle, bearing)), 0), 100);
    }
}
