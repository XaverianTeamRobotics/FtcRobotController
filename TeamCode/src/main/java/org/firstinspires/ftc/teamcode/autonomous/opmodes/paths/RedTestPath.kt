package org.firstinspires.ftc.teamcode.autonomous.opmodes.paths

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.autonomous.localizers.HybridLocalizer
import org.firstinspires.ftc.teamcode.autonomous.opmodes.templates.PathFollowingOpMode
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.autonomous.trajectorysequence.TrajectorySequenceBuilder
import java.lang.Math.toRadians


@Autonomous
class RedTestPath: PathFollowingOpMode() {

    override val startPose: Pose2d
        get() = Pose2d(48.0, -48.00, toRadians(90.0))

    override fun buildTrajectorySequence(builder: TrajectorySequenceBuilder): TrajectorySequence =
        builder
            .lineToConstantHeading(Vector2d(-10.0, -48.0))
            .build()


    override fun postInit() {
        HybridLocalizer.enableLogging = true
    }
}