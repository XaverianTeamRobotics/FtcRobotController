package org.firstinspires.ftc.teamcode.autonomous.drive

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.autonomous.localizers.LimelightLocalizer
import org.firstinspires.ftc.teamcode.autonomous.localizers.PinpointLocalizer
import org.firstinspires.ftc.teamcode.autonomous.localizers.hybrid
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceBuilder
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceRunner
import org.firstinspires.ftc.teamcode.autonomous.util.LynxModuleUtil
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.*
import kotlin.math.abs

/*
 * Simple mecanum drive hardware implementation for REV hardware.
 *
 * If localizer is null, the default will be used
 */
class MecanumDriver(hardwareMap: HardwareMap, localizer: Localizer? = null) :
    MecanumDrive(kV, kA, kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER) {
    private val trajectorySequenceRunner: TrajectorySequenceRunner

    private val follower: TrajectoryFollower?

    private val leftFront: DcMotorEx
    private val leftRear: DcMotorEx
    private val rightRear: DcMotorEx
    private val rightFront: DcMotorEx
    private val motors: List<DcMotorEx>

    private val imu: IMU
    private val batteryVoltageSensor: VoltageSensor

    override val rawExternalHeading: Double
        get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)

    init {
        follower = HolonomicPIDVAFollower(
            TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
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

        leftFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, DRIVE_FRONT_LEFT.NAME)
        leftRear = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, DRIVE_BACK_LEFT.NAME)
        rightRear = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, DRIVE_BACK_RIGHT.NAME)
        rightFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, DRIVE_FRONT_RIGHT.NAME)

        motors = listOf<DcMotorEx>(leftFront, leftRear, rightRear, rightFront)

        for (motor in motors) {
            val motorConfigurationType = motor.motorType.clone()
            motorConfigurationType.achieveableMaxRPMFraction = 1.0
            motor.motorType = motorConfigurationType
        }

        setZeroPowerBehavior(ZeroPowerBehavior.BRAKE)

        // TODO: reverse any motors using DcMotor.setDirection()
        leftFront.direction = DRIVE_FRONT_LEFT.DIRECTION
        leftRear.direction = DRIVE_BACK_LEFT.DIRECTION
        rightFront.direction = DRIVE_FRONT_RIGHT.DIRECTION
        rightRear.direction = DRIVE_BACK_RIGHT.DIRECTION

        if (localizer != null) {
            this.localizer = localizer
        } else {
            this.localizer = LimelightLocalizer() hybrid PinpointLocalizer()
        }

        trajectorySequenceRunner = TrajectorySequenceRunner(
            follower, HEADING_PID, batteryVoltageSensor,
        )
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double = 0.0): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectorySequenceBuilder(startPose: Pose2d?): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, ACCEL_CONSTRAINT,
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

        if ((abs(drivePower.x) + abs(drivePower.y) + abs(drivePower.heading)) > 1) {
            // re-normalize the powers according to the weights
            val denom =
                (VX_WEIGHT * abs(drivePower.x) + VY_WEIGHT * abs(drivePower.y) + OMEGA_WEIGHT * abs(drivePower.heading))

            vel = Pose2d(
                VX_WEIGHT * drivePower.x,
                VY_WEIGHT * drivePower.y,
                OMEGA_WEIGHT * drivePower.heading
            ).div(denom)
        }

        setDrivePower(vel!!)
    }

    override fun getWheelPositions(): List<Double> {
//        lastEncPositions.clear()
//
//        val wheelPositions = ArrayList<Double>()
//        for (motor in motors) {
//            val position = motor.getCurrentPosition()
//            lastEncPositions.add(position)
//            wheelPositions.add(encoderTicksToInches(position.toDouble()))
//        }
//        return wheelPositions

        // This is only used by the mecanum localizer which we don't use

        return emptyList()
    }

    override fun getWheelVelocities(): List<Double> {
//        lastEncVels.clear()
//
//        val wheelVelocities = ArrayList<Double>()
//        for (motor in motors) {
//            val vel = motor.getVelocity().toInt()
//            lastEncVels.add(vel)
//            wheelVelocities.add(encoderTicksToInches(vel.toDouble()))
//        }
//        return wheelVelocities

        // This is only used by the mecanum localizer which we don't use
        return emptyList()
    }

    override fun setMotorPowers(v: Double, v1: Double, v2: Double, v3: Double) {
        leftFront.power = v
        leftRear.power = v1
        rightRear.power = v2
        rightFront.power = v3
    }


    override fun getExternalHeadingVelocity(): Double? {
        return imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate.toDouble()
    }

    companion object {
        val TRANSLATIONAL_PID: PIDCoefficients
            get() = AutoSettings.TRANSLATIONAL_PID
        val HEADING_PID: PIDCoefficients
            get() = AutoSettings.HEADING_PID

        private val VEL_CONSTRAINT
            get() = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH)
        private val ACCEL_CONSTRAINT
            get() = getAccelerationConstraint(MAX_ACCEL)

        fun getVelocityConstraint(
            maxVel: Double,
            maxAngularVel: Double,
            trackWidth: Double
        ): TrajectoryVelocityConstraint {
            return MinVelocityConstraint(
                listOf<TrajectoryVelocityConstraint>(
                    AngularVelocityConstraint(maxAngularVel),
                    MecanumVelocityConstraint(maxVel, trackWidth)
                )
            )
        }

        fun getAccelerationConstraint(maxAccel: Double): TrajectoryAccelerationConstraint {
            return ProfileAccelerationConstraint(maxAccel)
        }
    }
}
