package org.firstinspires.ftc.teamcode.internals.settings

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Config
object AutoSettings {
    val loader: DremelLoader = createDremelLoader("auto.drml", this::class.java)

    init {
        loader.load()
    }

    @DremelData(DremelType.STRING)
    val DRIVE_FRONT_RIGHT_NAME: String = "motor0"
    @DremelData(DremelType.BOOLEAN)
    val DRIVE_FRONT_RIGHT_REVERSED: Boolean = false
    val DRIVE_FRONT_RIGHT: MotorConfig
        get() = MotorConfig(
            DRIVE_FRONT_RIGHT_NAME,
            if (DRIVE_FRONT_RIGHT_REVERSED) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
        )

    @DremelData(DremelType.STRING)
    val DRIVE_BACK_RIGHT_NAME: String = "motor0"
    @DremelData(DremelType.BOOLEAN)
    val DRIVE_BACK_RIGHT_REVERSED: Boolean = false
    val DRIVE_BACK_RIGHT: MotorConfig
        get() = MotorConfig(
            DRIVE_BACK_RIGHT_NAME,
            if (DRIVE_BACK_RIGHT_REVERSED) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
        )

    @DremelData(DremelType.STRING)
    val DRIVE_FRONT_LEFT_NAME: String = "motor0"
    @DremelData(DremelType.BOOLEAN)
    val DRIVE_FRONT_LEFT_REVERSED: Boolean = false
    val DRIVE_FRONT_LEFT: MotorConfig
        get() = MotorConfig(
            DRIVE_FRONT_LEFT_NAME,
            if (DRIVE_FRONT_LEFT_REVERSED) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
        )

    @DremelData(DremelType.STRING)
    val DRIVE_BACK_LEFT_NAME: String = "motor0"
    @DremelData(DremelType.BOOLEAN)
    val DRIVE_BACK_LEFT_REVERSED: Boolean = false
    val DRIVE_BACK_LEFT: MotorConfig
        get() = MotorConfig(
            DRIVE_BACK_LEFT_NAME,
            if (DRIVE_BACK_LEFT_REVERSED) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
        )

    @DremelData(DremelType.DOUBLE)
    val TICKS_PER_REV: Double = 537.7

    @DremelData(DremelType.DOUBLE)
    val MAX_RPM: Double = 312.0

    @DremelData(DremelType.DOUBLE)
    val WHEEL_RADIUS: Double = 1.8898

    @DremelData(DremelType.DOUBLE)
    val GEAR_RATIO: Double = 1.0

    @DremelData(DremelType.DOUBLE)
    val TRACK_WIDTH: Double = 13.7795

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
    @DremelData(DremelType.DOUBLE)
    val MAX_VEL: Double = 30.0

    /**
     * Maximum experimental acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    @DremelData(DremelType.DOUBLE)
    val MAX_ACCEL: Double = 30.0

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
    @DremelData(DremelType.DOUBLE)
    val MAX_ANG_VEL: Double = 2.0

    /**
     * Maximum experimental angular (turning) acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental angular velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    @DremelData(DremelType.DOUBLE)
    val MAX_ANG_ACCEL: Double = 2.0

    @DremelData(DremelType.DOUBLE)
    val kA: Double = 0.0033

    @DremelData(DremelType.DOUBLE)
    val kV: Double = 0.01578

    @DremelData(DremelType.DOUBLE)
    val kStatic: Double = 0.06238

    @DremelData(DremelType.DOUBLE)
    val LATERAL_MULTIPLIER: Double = 1.0

    @DremelData(DremelType.DOUBLE)
    val HEADING_PID_kP: Double = 8.0

    @DremelData(DremelType.DOUBLE)
    val HEADING_PID_kI: Double = 0.0

    @DremelData(DremelType.DOUBLE)
    val HEADING_PID_kD: Double = 1.0
    val HEADING_PID: PIDCoefficients
        get() = PIDCoefficients(HEADING_PID_kP, HEADING_PID_kI, HEADING_PID_kD)

    @DremelData(DremelType.DOUBLE)
    val TRANSLATIONAL_PID_kP: Double = 8.0

    @DremelData(DremelType.DOUBLE)
    val TRANSLATIONAL_PID_kI: Double = 0.0

    @DremelData(DremelType.DOUBLE)
    val TRANSLATIONAL_PID_kD: Double = 1.0
    val TRANSLATIONAL_PID: PIDCoefficients
        get() = PIDCoefficients(TRANSLATIONAL_PID_kP, TRANSLATIONAL_PID_kI, TRANSLATIONAL_PID_kD)

    @DremelData(DremelType.DOUBLE)
    val VX_WEIGHT: Double = 1.0

    @DremelData(DremelType.DOUBLE)
    val VY_WEIGHT: Double = 1.0

    @DremelData(DremelType.DOUBLE)
    val OMEGA_WEIGHT: Double = 1.0

    @DremelData(DremelType.INT)
    val LIMELIGHT_MT_PIPELINE_ID: Int = 1

    /**
     * The X offset of the pinpoint.
     * This is in mm for some stupid reason idk dont ask me
     */
    @DremelData(DremelType.DOUBLE)
    val PINPOINT_X_OFFSET: Double = 0.0

    /**
     * The Y offset of the pinpoint.
     * This is in mm for some stupid reason idk dont ask me
     */
    @DremelData(DremelType.DOUBLE)
    val PINPOINT_Y_OFFSET: Double = 0.0

    @DremelData(DremelType.BOOLEAN)
    val PINPOINT_X_REVERSED: Boolean = false

    @DremelData(DremelType.BOOLEAN)
    val PINPOINT_Y_REVERSED: Boolean = false

    /**
     * The maximum angular velocity before the primary localizer becomes unavailable (the limelight, unless overwritten)
     */
    @DremelData(DremelType.DOUBLE)
    val MAX_SAFE_ANGULAR_VELOCITY: Double = 5.0

    /**
     * The maximum linear velocity before the primary localizer becomes unavailable (the limelight, unless overwritten)
     */
    @DremelData(DremelType.DOUBLE)
    val MAX_SAFE_LINEAR_VELOCITY: Double = 5.0

    /**
     * The yaw multiplier for the pinpoint.
     * Obtained from {@link org.firstinspires.ftc.teamcode.autonomous.opmodes.tuning.PinpointYawTuner}.
     * 0 = default
     */
    @DremelData(DremelType.DOUBLE)
    val PINPOINT_YAW_SCALAR: Double = 0.0

    @DremelData(DremelType.DOUBLE)
    val LIMELIGHT_SERVO_CENTER: Double = 0.484

    @DremelData(DremelType.DOUBLE)
    val LIMELIGHT_SERVO_BUCKET: Double = 1.0
}