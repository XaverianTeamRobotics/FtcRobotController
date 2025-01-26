package org.firstinspires.ftc.teamcode.internals.settings;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Settings for an odometry implementation using three odometry pods or two odometry pods and an intertial measurement unit. Tuning this is incredibly important for SLAM applications like autonomous driving or field-centric driving. Errors in this implementation compound continously, so it's important to make sure these values are as precise as possible.
 * <br><br>
 * This document makes numerous references to the Strafer v5 chassis by goBILDA. Its SKU is 3209-0001-0005.
 * <br>
 * DONT MAKE THIS INTO KOTLIN!!!!!
 */
public class DefaultAutoSettings {
    /**
     * The name and direction of the front right motor.
     */
    public static MotorConfig DRIVE_FRONT_RIGHT = new MotorConfig("motor0", DcMotorSimple.Direction.FORWARD);

    /**
     * The name and direction of the back right motor.
     */
    public static MotorConfig DRIVE_BACK_RIGHT = new MotorConfig("motor1", DcMotorSimple.Direction.FORWARD);

    /**
     * The name and direction of the front left motor.
     */
    public static MotorConfig DRIVE_FRONT_LEFT = new MotorConfig("motor2", DcMotorSimple.Direction.REVERSE);

    /**
     * The name and direction of the back left motor.
     */
    public static MotorConfig DRIVE_BACK_LEFT = new MotorConfig("motor3", DcMotorSimple.Direction.REVERSE);

    /**
     * The name of the IMU or gyroscope replacing the right encoder if in use.
     */
    public static String IMU_NAME = "imu";

    /**
     * The ticks per revolution of the internal encoder inside your drive motors. This should come from the motor's specsheet or a similar specification document. For example, the Strafer v5's motors run count 537.7 ticks per revolution.
     */
    public static double TICKS_PER_REV = 537.7;

    /**
     * The maximum rotations per minute of your drive motors. This should come from the motor's specsheet or a similar specification document. For example, the Strafer v5's motors run at 312 RPM.
     */
    public static double MAX_RPM = 312;

    /**
     * The radius of the driving wheels in inches. This should come from the wheel's specsheet or a similar specification document. For example, the Strafer v5's mecanum wheels have a radius of 1.8898 inches.
     */
    public static double WHEEL_RADIUS = 1.8898;

    /**
     * The gear ratio between the output (wheel) speed and the input (motor) speed. For example, the Strafer v5 has a gear ratio of 1:1, or just 1.
     */
    public static double GEAR_RATIO = 1;

    /**
     * The track width of the driving wheels in inches. Track width is the lateral distance from the center of one wheel to the center of another wheel. Only a rough estimate is needed originally as it will be tuned later. For example, the Strafer v5's track width is ≈16 inches.
     */
    public static double TRACK_WIDTH = 13.7795;

    /**
     * The wheel base of the driving wheels in inches.
     */
    public static double WHEEL_BASE = 13.7795;

    /**
     * Maximum experimental velocity of your bot. Calculate it using the equation:
     * <br><br>
     * <kbd>
     *  ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.75;
     * </kbd>
     * <br><br>
     * This calculated value is actually only 75% of the theoretical maximum velocity of the bot. This is because what is theoretically true is never experimentally true. You can increase this value once your bot is tuned properly and follows paths accurately. Go no higher than 85% of the bot's theoretical maximum velocity unless necessary.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra velocity is necessary. Basically, run your bot as slow as you can without sacrificing points.
     * <br><br>
     * If you don't have the time to calculate the slowest possible maximum velocity, 30 should suffice for most robots.
     */
    public static double MAX_VEL = 30;

    /**
     * Maximum experimental acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    public static double MAX_ACCEL = MAX_VEL;

    /**
     * Maximum experimental angular (turning) velocity of your bot. Calculate it using the equation:
     * <br><br>
     * <kbd>
     *  Math.toRadians(Range.clip(MAX_VEL / TRACK_WIDTH * (180 / Math.PI), -360, 360));
     * </kbd>
     * <br><br>
     * Notice that this value is represented in radians and within the interval <kbd>[-2π, 2π]</kbd>.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra velocity is necessary. Basically, run your bot as slow as you can without sacrificing points.
     * <br><br>
     * If you don't have the time to calculate the slowest possible maximum angular velocity, 3 should suffice for most robots.
     */
    public static double MAX_ANG_VEL = 2;

    /**
     * Maximum experimental angular (turning) acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental angular velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    public static double MAX_ANG_ACCEL = MAX_ANG_VEL;

    /**
     * The PID acceleration variable. This is to be tuned by the manual feedforward tuner.
     */
    public static double kA = 0.0033;

    /**
     * The PID velocity variable. This is to be tuned by the automatic feedforward tuner.
     */
    public static double kV = 0.01578;

    /**
     * The PID static variable. This is to be tuned by the automatic feedforward tuner.
     */
    public static double kStatic = 0.06238;

    /**
     * Mecanum wheels often exhibit less torque strafing than they do going straigt. This is to be tuned by the strafe tuner.
     */
    public static double LATERAL_MULTIPLIER = 1;

    /**
     * The heading PID controller. This is to be tuned by the final back-and-forth and follower PID tuners. Standard values are a kP of 8, kI of 0, and kD of 1.
     */
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(8, 0, 1);

    /**
     * The translational PID controller. This is to be tuned by the final back-and-forth and follower PID tuners. Standard values are a kP of 8, kI of 0, and kD of 1.
     */
    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(8, 0, 1);

    /**
     * The X weight used in normalization. This is usually fine being set to 1.
     */
    public static double VX_WEIGHT = 1;

    /**
     * The Y weight used in normalization. This is usually fine being set to 1.
     */
    public static double VY_WEIGHT = 1;

    /**
     * The Ω weight used in normalization. This is usually fine being set to 1.
     */
    public static double OMEGA_WEIGHT = 1;

    /**
     * The pipeline ID for the limelight MegaTag pipeline (usually 3)
     */
    public static double LIMELIGHT_MT_PIPELINE_ID = 3;

    /**
     * The X offset of the pinpoint.
     * This is in mm for some stupid reason idk dont ask me
     */
    public static double PINPOINT_X_OFFSET = 0;

    /**
     * The Y offset of the pinpoint.
     * This is in mm for some stupid reason idk dont ask me
     */
    public static double PINPOINT_Y_OFFSET = 0;

    /**
     * Whether the X axis of the pinpoint is reversed.
     */
    public static boolean PINPOINT_X_REVERSED = false;

    /**
     * Whether the Y axis of the pinpoint is reversed.
     */
    public static boolean PINPOINT_Y_REVERSED = false;

    /**
     * The maximum angular velocity before the primary localizer becomes unavailable (the limelight, unless overwritten)
     */
    public static double MAX_SAFE_ANGULAR_VELOCITY = 10.0;

    /**
     * The maximum linear velocity before the primary localizer becomes unavailable (the limelight, unless overwritten)
     */
    public static double MAX_SAFE_LINEAR_VELOCITY = 10.0;

    /**
     * The yaw multiplier for the pinpoint.
     * Obtained from {@link org.firstinspires.ftc.teamcode.autonomous.opmodes.tuning.PinpointYawTuner}.
     * 0 = default
     */
    public static double PINPOINT_YAW_SCALAR = 0;

    /**
     * The Center position of the limelight servo
     */
    public static double LIMELIGHT_SERVO_CENTER = 0.484;

    /**
     * The Bucket position of the limelight servo
     */
    public static double LIMELIGHT_SERVO_BUCKET = 1;
}
