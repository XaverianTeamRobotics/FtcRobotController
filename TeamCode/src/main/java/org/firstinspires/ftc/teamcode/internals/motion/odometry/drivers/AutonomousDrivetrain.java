package org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers;

import androidx.annotation.NonNull;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.*;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoBuilder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceRunner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.LocalizationType;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.LynxModuleUtil;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdometrySettingsDashboardConfiguration;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutonomousDrivetrain extends MecanumDrive {


    /*

        +---------------+
        |   Variables   |
        +---------------+

     */


    public static PIDCoefficients TRANSLATIONAL_PID = OdometrySettingsDashboardConfiguration.TRANSLATIONAL_PID;
    public static PIDCoefficients HEADING_PID = OdometrySettingsDashboardConfiguration.HEADING_PID;
    public static double LATERAL_MULTIPLIER = OdometrySettingsDashboardConfiguration.LATERAL_MULTIPLIER;
    public static double VX_WEIGHT = OdometrySettingsDashboardConfiguration.VX_WEIGHT;
    public static double VY_WEIGHT = OdometrySettingsDashboardConfiguration.VY_WEIGHT;
    public static double OMEGA_WEIGHT = OdometrySettingsDashboardConfiguration.OMEGA_WEIGHT;
    private static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(OdometrySettingsDashboardConfiguration.MAX_VEL, OdometrySettingsDashboardConfiguration.MAX_ANG_VEL, OdometrySettingsDashboardConfiguration.TRACK_WIDTH);
    private static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(OdometrySettingsDashboardConfiguration.MAX_ACCEL);
    private static final double SLOW_VEL_MULT = 1.5;
    private static final double SLOW_ACCEL_MULT = 2;
    public static final TrajectoryVelocityConstraint SLOW_VEL_CONSTRAINT = getVelocityConstraint(OdometrySettingsDashboardConfiguration.MAX_VEL / SLOW_VEL_MULT, OdometrySettingsDashboardConfiguration.MAX_ANG_VEL / SLOW_VEL_MULT, OdometrySettingsDashboardConfiguration.TRACK_WIDTH);
    public static final TrajectoryAccelerationConstraint SLOW_ACCEL_CONSTRAINT = getAccelerationConstraint(OdometrySettingsDashboardConfiguration.MAX_ACCEL / SLOW_ACCEL_MULT);
    private TrajectorySequenceRunner trajectorySequenceRunner;
    private TrajectoryFollower follower;
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    private List<DcMotorEx> motors;
    private VoltageSensor batteryVoltageSensor;
    private IMU imu;
    private boolean useIMU;
    private final AutoBuilder pathing;


    /*

        +-------------------+
        |   Instantiation   |
        +-------------------+

     */

    public AutonomousDrivetrain() {
        this(HardwareGetter.getHardwareMap(), null);
    }

    public AutonomousDrivetrain(AutoBuilder pathing) {
        this(HardwareGetter.getHardwareMap(), pathing);
    }

    public AutonomousDrivetrain(HardwareMap hardwareMap) {
        this(hardwareMap, null);
    }

    public AutonomousDrivetrain(HardwareMap hardwareMap, AutoBuilder pathing) {
        super(OdometrySettingsDashboardConfiguration.kV, OdometrySettingsDashboardConfiguration.kA, OdometrySettingsDashboardConfiguration.kStatic, OdometrySettingsDashboardConfiguration.TRACK_WIDTH, OdometrySettingsDashboardConfiguration.TRACK_WIDTH, LATERAL_MULTIPLIER);
        this.pathing = pathing;
        follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
                new Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5);
        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        leftFront = hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.DRIVE_FRONT_LEFT.NAME);
        leftRear = hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.DRIVE_BACK_LEFT.NAME);
        rightRear = hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.DRIVE_BACK_RIGHT.NAME);
        rightFront = hardwareMap.get(DcMotorEx.class, OdometrySettingsDashboardConfiguration.DRIVE_FRONT_RIGHT.NAME);
        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);
        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }
        if (ConstantUtils.RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (ConstantUtils.RUN_USING_ENCODER && ConstantUtils.MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, ConstantUtils.MOTOR_VELO_PID);
        }
        leftFront.setDirection(OdometrySettingsDashboardConfiguration.DRIVE_FRONT_LEFT.DIRECTION);
        leftRear.setDirection(OdometrySettingsDashboardConfiguration.DRIVE_BACK_LEFT.DIRECTION);
        rightRear.setDirection(OdometrySettingsDashboardConfiguration.DRIVE_BACK_RIGHT.DIRECTION);
        rightFront.setDirection(OdometrySettingsDashboardConfiguration.DRIVE_FRONT_RIGHT.DIRECTION);
        useIMU = OdometrySettingsDashboardConfiguration.LOCALIZATION_TYPE == LocalizationType.IMU;
        if(useIMU) {
            imu = hardwareMap.get(IMU.class, OdometrySettingsDashboardConfiguration.IMU);
            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
            imu.initialize(parameters);
            setLocalizer(new IMULocalizer(hardwareMap, this));
        }else{
            setLocalizer(new PodLocalizer(hardwareMap));
        }
        trajectorySequenceRunner = new TrajectorySequenceRunner(follower, HEADING_PID);
        PoseBucket.forceSetPose(new Pose2d(0, 0, 0));
        setPoseEstimate(PoseBucket.getPose());
    }


    /*

        +-------------------+
        |   Driving Logic   |
        +-------------------+

     */


    public Pose2d getLastError() {
        return trajectorySequenceRunner.getLastPoseError();
    }

    public void update() {
        updatePoseEstimate();
        DriveSignal signal = trajectorySequenceRunner.update(getPoseEstimate(), getPoseVelocity());
        if (signal != null) setDriveSignal(signal);
        PoseBucket.setPose(getPoseEstimate());
    }

    public void waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && isBusy())
            update();
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        List<Double> wheelPositions = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            wheelPositions.add(ConstantUtils.encoderTicksToInches(motor.getCurrentPosition()));
        }
        return wheelPositions;
    }

    @Override
    public List<Double> getWheelVelocities() {
        List<Double> wheelVelocities = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            wheelVelocities.add(ConstantUtils.encoderTicksToInches(motor.getVelocity()));
        }
        return wheelVelocities;
    }

    @Override
    public void setMotorPowers(double v, double v1, double v2, double v3) {
        leftFront.setPower(v);
        leftRear.setPower(v1);
        rightRear.setPower(v2);
        rightFront.setPower(v3);
    }

    @Override
    public double getRawExternalHeading() {
        if(useIMU) {
            return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
        return 0;
    }

    @Override
    public Double getExternalHeadingVelocity() {
        if(useIMU) {
            return (double) imu.getRobotAngularVelocity(AngleUnit.RADIANS).xRotationRate;
        }
        return 0.0;
    }

    public void turnAsync(double angle) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(getPoseEstimate())
                .turn(angle)
                .completeTrajectory()
        );
    }

    public void turn(double angle) {
        turnAsync(angle);
        waitForIdle();
    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(trajectory.start())
                .addTrajectory(trajectory)
                .completeTrajectory()
        );
    }

    public void followTrajectory(Trajectory trajectory) {
        followTrajectoryAsync(trajectory);
        waitForIdle();
    }

    public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence);
    }

    public void followTrajectorySequence(TrajectorySequence trajectorySequence) {
        followTrajectorySequenceAsync(trajectorySequence);
        waitForIdle();
    }


    /*

        +-------------------------+
        |   Trajectory Creators   |
        +-------------------------+

     */


    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
        return new TrajectoryBuilder(startPose, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed) {
        return new TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading) {
        return new TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose) {
        TrajectorySequenceBuilder builder = new TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, ACCEL_CONSTRAINT,
            OdometrySettingsDashboardConfiguration.MAX_ANG_VEL, OdometrySettingsDashboardConfiguration.MAX_ANG_ACCEL,
            this, pathing
        );
        return builder;
    }


    /*

        +----------------+
        |   Utilities   |
        +---------------+

     */


    public boolean isBusy() {
        return trajectorySequenceRunner.isBusy();
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
            coefficients.p, coefficients.i, coefficients.d,
            coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void setWeightedDrivePower(Pose2d drivePower) {
        Pose2d vel = drivePower;

        if (Math.abs(drivePower.getX()) + Math.abs(drivePower.getY())
            + Math.abs(drivePower.getHeading()) > 1) {
            // re-normalize the powers according to the weights
            double denom = VX_WEIGHT * Math.abs(drivePower.getX())
                + VY_WEIGHT * Math.abs(drivePower.getY())
                + OMEGA_WEIGHT * Math.abs(drivePower.getHeading());

            vel = new Pose2d(
                VX_WEIGHT * drivePower.getX(),
                VY_WEIGHT * drivePower.getY(),
                OMEGA_WEIGHT * drivePower.getHeading()
            ).div(denom);
        }

        setDrivePower(vel);
    }

    public static TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
        return new MinVelocityConstraint(Arrays.asList(
            new AngularVelocityConstraint(maxAngularVel),
            new MecanumVelocityConstraint(maxVel, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
        return new ProfileAccelerationConstraint(maxAccel);
    }

}
