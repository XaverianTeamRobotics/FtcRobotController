package org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdoEncoder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdometrySettingsDashboardConfiguration;

import java.util.Arrays;
import java.util.List;

@Config
public class IMULocalizer extends TwoTrackingWheelLocalizer {


    /*

        +---------------+
        |   Variables   |
        +---------------+

     */


    public static double TICKS_PER_REV = OdometrySettingsDashboardConfiguration.ENCODER_TICKS_PER_REV;
    public static double WHEEL_RADIUS = OdometrySettingsDashboardConfiguration.ENCODER_WHEEL_RADIUS;
    public static double GEAR_RATIO = OdometrySettingsDashboardConfiguration.ENCODER_GEAR_RATIO;
    public static double PARALLEL_X = 0;
    public static double PARALLEL_Y = -OdometrySettingsDashboardConfiguration.ENCODER_TRACK_WIDTH / 2.0;
    public static double PERPENDICULAR_X = OdometrySettingsDashboardConfiguration.ENCODER_FORWARD_OFFSET;
    public static double PERPENDICULAR_Y = 0;
    private OdoEncoder leftEncoder, frontEncoder;
    private AutonomousDrivetrain drive;


    /*

        +-------------------+
        |   Instantiation   |
        +-------------------+

     */


    public IMULocalizer(HardwareMap hardwareMap, AutonomousDrivetrain drive) {
        super(Arrays.asList(
            new Pose2d(PARALLEL_X, PARALLEL_Y, 0),
            new Pose2d(PERPENDICULAR_X, PERPENDICULAR_Y, Math.toRadians(90))
        ));

        this.drive = drive;

        leftEncoder = new OdoEncoder(hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.ENCODER_LEFT.NAME));
        frontEncoder = new OdoEncoder(hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.ENCODER_MIDDLE.NAME));

        leftEncoder.setDirection(OdometrySettingsDashboardConfiguration.ENCODER_LEFT.DIRECTION);
        frontEncoder.setDirection(OdometrySettingsDashboardConfiguration.ENCODER_MIDDLE.DIRECTION);
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }


    /*

        +------------------+
        |   Localization   |
        +------------------+

     */


    @Override
    public double getHeading() {
        return drive.getRawExternalHeading();
    }

    @Override
    public Double getHeadingVelocity() {
        return drive.getExternalHeadingVelocity();
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()),
                encoderTicksToInches(frontEncoder.getCurrentPosition())
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()),
                encoderTicksToInches(frontEncoder.getCorrectedVelocity())
        );
    }
}