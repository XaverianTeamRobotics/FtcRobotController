package org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdoEncoder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdometrySettingsDashboardConfiguration;

import java.util.Arrays;
import java.util.List;

@Config
public class PodLocalizer extends ThreeTrackingWheelLocalizer {


    /*

        +---------------+
        |   Variables   |
        +---------------+

     */


    public static double TICKS_PER_REV = OdometrySettingsDashboardConfiguration.ENCODER_TICKS_PER_REV;
    public static double WHEEL_RADIUS = OdometrySettingsDashboardConfiguration.ENCODER_WHEEL_RADIUS;
    public static double GEAR_RATIO = OdometrySettingsDashboardConfiguration.ENCODER_GEAR_RATIO;
    public static double LATERAL_DISTANCE = OdometrySettingsDashboardConfiguration.ENCODER_TRACK_WIDTH;
    public static double FORWARD_OFFSET = OdometrySettingsDashboardConfiguration.ENCODER_FORWARD_OFFSET;
    private OdoEncoder leftEncoder, rightEncoder, frontEncoder;


    /*

        +-------------------+
        |   Instantiation   |
        +-------------------+

     */


    public PodLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        leftEncoder = new OdoEncoder(hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.ENCODER_LEFT.NAME));
        rightEncoder = new OdoEncoder(hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.ENCODER_RIGHT.NAME));
        frontEncoder = new OdoEncoder(hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.ENCODER_MIDDLE.NAME));

        leftEncoder.setDirection(OdometrySettingsDashboardConfiguration.ENCODER_LEFT.DIRECTION);
        rightEncoder.setDirection(OdometrySettingsDashboardConfiguration.ENCODER_RIGHT.DIRECTION);
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


    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()),
                encoderTicksToInches(rightEncoder.getCurrentPosition()),
                encoderTicksToInches(frontEncoder.getCurrentPosition())
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()),
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()),
                encoderTicksToInches(frontEncoder.getCorrectedVelocity())
        );
    }

}
