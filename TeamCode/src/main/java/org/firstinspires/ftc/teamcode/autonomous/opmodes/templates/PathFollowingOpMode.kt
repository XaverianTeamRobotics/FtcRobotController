package org.firstinspires.ftc.teamcode.autonomous.opmodes.templates

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.autonomous.drive.MecanumDriver
import org.firstinspires.ftc.teamcode.autonomous.limelight.LimelightServoScript
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceBuilder
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

abstract class PathFollowingOpMode: BaseOpMode() {
    private lateinit var drive: MecanumDriver
    private lateinit var trajectorySequence: TrajectorySequence
    protected var limelightServoPosition = LimelightServoScript.LimelightServoPosition.CENTER

    abstract val startPose: Pose2d

    override fun construct() {
        drive = MecanumDriver(hardwareMap)
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        addScript(LimelightServoScript { limelightServoPosition })

        trajectorySequence = buildTrajectorySequence(drive.trajectorySequenceBuilder(startPose))
    }

    abstract fun buildTrajectorySequence(builder: TrajectorySequenceBuilder): TrajectorySequence

    override fun run() {
        drive.followTrajectorySequence(trajectorySequence)
        requestOpModeStop()
    }

    override fun onStop() {
        drive.setDrivePower(Pose2d(0.0, 0.0, 0.0))
    }
}