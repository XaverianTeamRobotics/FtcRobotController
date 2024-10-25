//package org.firstinspires.ftc.teamcode.autonomous.drive
//
//import com.acmerobotics.roadrunner.drive.MecanumDrive
//import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower
//import com.acmerobotics.roadrunner.followers.TrajectoryFollower
//import com.acmerobotics.roadrunner.geometry.Pose2d
//import com.acmerobotics.roadrunner.localization.Localizer
//import com.qualcomm.hardware.lynx.LynxModule
//import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
//import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
//import com.qualcomm.robotcore.hardware.IMU
//import com.qualcomm.robotcore.hardware.VoltageSensor
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
//import org.firstinspires.ftc.teamcode.autonomous.drive.samples.SampleMecanumDrive
//import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceRunner
//import org.firstinspires.ftc.teamcode.autonomous.util.LynxModuleUtil
//import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
//import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.hardwareMap
//import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
//import org.firstinspires.ftc.teamcode.internals.settings.OdometrySettings.*
//
//class CustomLoacalizerMecanumDriver(override var localizer: Localizer):
//MecanumDrive(kV, kA, kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER) {
//    private val imu = HardwareManager.imu
//    private val follower: TrajectoryFollower = HolonomicPIDVAFollower(
//        SampleMecanumDrive.Companion.TRANSLATIONAL_PID,
//        SampleMecanumDrive.Companion.TRANSLATIONAL_PID,
//        SampleMecanumDrive.Companion.HEADING_PID,
//        Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5
//    )
//    private val batteryVoltageSensor: VoltageSensor = hardwareMap.voltageSensor.iterator().next()
//
//    private val flMotor = motors.get("fl", 0)
//    private val blMotor = motors.get("bl", 1)
//    private val frMotor = motors.get("fr", 2)
//    private val brMotor = motors.get("br", 3)
//    private val motorArray = listOf(flMotor, blMotor, frMotor, brMotor)
//
//
//    private val trajectorySequenceRunner = TrajectorySequenceRunner(follower,
//        SampleMecanumDrive.Companion.HEADING_PID, batteryVoltageSensor,
//        lastEncPositions, lastEncVels, lastTrackingEncPositions, lastTrackingEncVels)
//
//    init {
//        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap)
//
//        for (module in hardwareMap.getAll<LynxModule>(LynxModule::class.java)) {
//            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)
//        }
//
//        // TODO: Allow customization of control hub orientation
//        val parameters = IMU.Parameters(
//            RevHubOrientationOnRobot(
//                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT
//            )
//        )
//        imu.initialize(parameters)
//
//        // configure all motors
//        for (motor in motorArray) {
//            val motorConfigurationType = motor.motorType.clone()
//            motorConfigurationType.achieveableMaxRPMFraction = 1.0
//            motor.motorType = motorConfigurationType
//            motor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
//        }
//    }
//
//    override fun getWheelPositions(): List<Double> {
//        @TODO("Implement this")
//    }
//
//    override fun setMotorPowers(
//        frontLeft: Double,
//        rearLeft: Double,
//        rearRight: Double,
//        frontRight: Double
//    ) {
//
//    }
//
//    override val rawExternalHeading: Double
//        get() = imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)
//
//}