package org.firstinspires.ftc.teamcode.autonomous.drive.samples

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.TankDrive
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TankVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.autonomous.localizers.StandardTrackingWheelLocalizer.Companion.encoderTicksToInches
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceBuilder
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceRunner
import org.firstinspires.ftc.teamcode.autonomous.util.LynxModuleUtil
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.MAX_ACCEL
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.MAX_ANG_ACCEL
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.MAX_ANG_VEL
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.MAX_VEL
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.TRACK_WIDTH
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.kA
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.kStatic
import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.kV
import java.util.ArrayList
import java.util.Arrays
import kotlin.math.abs

/*
 * Simple tank drive hardware implementation for REV hardware.
 */
@Config
class SampleTankDrive(hardwareMap: HardwareMap) : TankDrive(kV, kA, kStatic, TRACK_WIDTH) {
    private val trajectorySequenceRunner: TrajectorySequenceRunner

    private val follower: TrajectoryFollower?

    private val motors: MutableList<DcMotorEx>
    private val leftMotors: MutableList<DcMotorEx>
    private val rightMotors: MutableList<DcMotorEx>
    private val imu: IMU

    private val batteryVoltageSensor: VoltageSensor

    override val rawExternalHeading get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)

    init {
        follower = TankPIDVAFollower(
            AXIAL_PID, CROSS_TRACK_PID,
            Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5
        )

        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap)

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next()

        for (module in hardwareMap.getAll<LynxModule>(LynxModule::class.java)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)
        }

        // TODO: adjust the names of the following hardware devices to match your configuration
        imu = hardwareMap.get<IMU>(IMU::class.java, "imu")
        val parameters = IMU.Parameters(
            RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT
            )
        )
        imu.initialize(parameters)

        // add/remove motors depending on your robot (e.g., 6WD)
        val leftFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "leftFront")
        val leftRear = hardwareMap.get<DcMotorEx?>(DcMotorEx::class.java, "leftRear")
        val rightRear = hardwareMap.get<DcMotorEx?>(DcMotorEx::class.java, "rightRear")
        val rightFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "rightFront")

        motors = Arrays.asList<DcMotorEx?>(leftFront, leftRear, rightRear, rightFront)
        leftMotors = Arrays.asList<DcMotorEx?>(leftFront, leftRear)
        rightMotors = Arrays.asList<DcMotorEx?>(rightFront, rightRear)

        for (motor in motors) {
            val motorConfigurationType = motor.getMotorType().clone()
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0)
            motor.setMotorType(motorConfigurationType)
        }

        setZeroPowerBehavior(ZeroPowerBehavior.BRAKE)

        // TODO: reverse any motors using DcMotor.setDirection()

        // TODO: if desired, use setLocalizer() to change the localization method
        // for instance, setLocalizer(new ThreeTrackingWheelLocalizer(...));
        trajectorySequenceRunner = TrajectorySequenceRunner(
            follower, HEADING_PID, batteryVoltageSensor,
            ArrayList<Int?>(), ArrayList<Int?>(), ArrayList<Int?>(), ArrayList<Int?>()
        )
    }

    fun trajectoryBuilder(startPose: Pose2d): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, accelConstraint,
            MAX_ANG_VEL, MAX_ANG_ACCEL
        )
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(
            startPose,
            reversed,
            VEL_CONSTRAINT,
            accelConstraint
        )
    }

    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double): TrajectoryBuilder {
        return TrajectoryBuilder(
            startPose,
            startHeading,
            VEL_CONSTRAINT,
            accelConstraint
        )
    }

    fun trajectorySequenceBuilder(startPose: Pose2d?): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, accelConstraint,
            MAX_ANG_VEL, MAX_ANG_ACCEL
        )
    }

    fun turnAsync(angle: Double) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(poseEstimate)
                .turn(angle)
                .build()
        )
    }

    fun turn(angle: Double) {
        turnAsync(angle)
        waitForIdle()
    }

    fun followTrajectoryAsync(trajectory: Trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(trajectory.start())
                .addTrajectory(trajectory)
                .build()
        )
    }

    fun followTrajectory(trajectory: Trajectory) {
        followTrajectoryAsync(trajectory)
        waitForIdle()
    }

    fun followTrajectorySequenceAsync(trajectorySequence: TrajectorySequence?) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence)
    }

    fun followTrajectorySequence(trajectorySequence: TrajectorySequence?) {
        followTrajectorySequenceAsync(trajectorySequence)
        waitForIdle()
    }

    fun getLastError(): Pose2d? {
        return trajectorySequenceRunner.getLastPoseError()
    }


    fun update() {
        updatePoseEstimate()
        val signal = trajectorySequenceRunner.update(poseEstimate, poseVelocity)
        if (signal != null) setDriveSignal(signal)
    }

    fun waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && isBusy()) update()
    }

    fun isBusy(): Boolean {
        return trajectorySequenceRunner.isBusy()
    }

    fun setMode(runMode: RunMode?) {
        for (motor in motors) {
            motor.setMode(runMode)
        }
    }

    fun setZeroPowerBehavior(zeroPowerBehavior: ZeroPowerBehavior?) {
        for (motor in motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior)
        }
    }

    fun setPIDFCoefficients(runMode: RunMode?, coefficients: PIDFCoefficients) {
        val compensatedCoefficients = PIDFCoefficients(
            coefficients.p, coefficients.i, coefficients.d,
            coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        )
        for (motor in motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients)
        }
    }

    fun setWeightedDrivePower(drivePower: Pose2d) {
        var vel: Pose2d? = drivePower

        if (abs(drivePower.x) + abs(drivePower.heading) > 1) {
            // re-normalize the powers according to the weights
            val denom = (VX_WEIGHT * abs(drivePower.x)
                    + OMEGA_WEIGHT * abs(drivePower.heading))

            vel = Pose2d(
                VX_WEIGHT * drivePower.x,
                0.0,
                OMEGA_WEIGHT * drivePower.heading
            ).div(denom)
        } else {
            // Ensure the y axis is zeroed out.
            vel = Pose2d(drivePower.x, 0.0, drivePower.heading)
        }

        setDrivePower(vel)
    }

    public override fun getWheelPositions(): List<Double> {
        var leftSum = 0.0
        var rightSum = 0.0
        for (leftMotor in leftMotors) {
            leftSum += encoderTicksToInches(leftMotor.getCurrentPosition().toDouble())
        }
        for (rightMotor in rightMotors) {
            rightSum += encoderTicksToInches(rightMotor.getCurrentPosition().toDouble())
        }
        return listOf<Double>(leftSum / leftMotors.size, rightSum / rightMotors.size)
    }

    public override fun getWheelVelocities(): List<Double> {
        var leftSum = 0.0
        var rightSum = 0.0
        for (leftMotor in leftMotors) {
            leftSum += encoderTicksToInches(leftMotor.getVelocity())
        }
        for (rightMotor in rightMotors) {
            rightSum += encoderTicksToInches(rightMotor.getVelocity())
        }
        return listOf<Double>(leftSum / leftMotors.size, rightSum / rightMotors.size)
    }

    override fun setMotorPowers(v: Double, v1: Double) {
        for (leftMotor in leftMotors) {
            leftMotor.setPower(v)
        }
        for (rightMotor in rightMotors) {
            rightMotor.setPower(v1)
        }
    }

    override fun getExternalHeadingVelocity(): Double? {
        return imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate.toDouble()
    }

    companion object {
        @JvmField
        var AXIAL_PID: PIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)
        @JvmField
        var CROSS_TRACK_PID: PIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)
        @JvmField
        var HEADING_PID: PIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)

        var VX_WEIGHT: Double = 1.0
        var OMEGA_WEIGHT: Double = 1.0

        private val VEL_CONSTRAINT = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH)
        private val accelConstraint = getAccelerationConstraint(MAX_ACCEL)

        fun getVelocityConstraint(
            maxVel: Double,
            maxAngularVel: Double,
            trackWidth: Double
        ): TrajectoryVelocityConstraint {
            return MinVelocityConstraint(
                Arrays.asList<TrajectoryVelocityConstraint?>(
                    AngularVelocityConstraint(maxAngularVel),
                    TankVelocityConstraint(maxVel, trackWidth)
                )
            )
        }

        fun getAccelerationConstraint(maxAccel: Double): TrajectoryAccelerationConstraint {
            return ProfileAccelerationConstraint(maxAccel)
        }
    }
}
