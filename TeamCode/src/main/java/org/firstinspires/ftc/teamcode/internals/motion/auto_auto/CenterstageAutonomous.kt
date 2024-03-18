package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature
import org.firstinspires.ftc.teamcode.internals.image.VisionPipeline.TeamColor
import org.firstinspires.ftc.teamcode.internals.image.centerstage.SpikeMarkDetectionPipeline
import org.firstinspires.ftc.teamcode.internals.math.units.deg
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode

abstract class CenterstageAutonomous : OperationMode(), AutonomousOperation {
    protected lateinit var armClaw: ArmClaw
    protected lateinit var runner: AutoRunner
    protected lateinit var visionProcessor: VisionProcessingFeature
    protected var spot = 0
    protected fun buildSpikeMark(
        builder: TrajectorySequenceBuilder,
        drivetrain: AutonomousDrivetrain,
        teamColor: Int,
        purplePixelPosition: Int = 0
    ): TrajectorySequenceBuilder {
        var builder1 = builder
            .completeTrajectory()
            .appendAction {
                armClaw.closeRightGrabber()
                armClaw.closeLeftGrabber()

                var rotation = 0.deg
                if (spot == 1) rotation = 90.deg
                else if (spot == 3) rotation = (-90).deg
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                if (spot == 1) {
                    b.forward(4.0)
                } else if (spot == 3) {
                    b.forward(7.0)
                }
                b.turn(Math.toRadians(180.0) + rotation.rad)
                if (spot == 3 && teamColor == 1) b.back(5.0)
                else if (spot == 2) b.back(5.5)
                else b.back(5.0)
                b.forward(5.0)
                if (spot == 3 || spot == 1) b.forward(2.0)
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendAction { armClaw.autoRaiseArm(138) }
            .appendAction { waitUntil { armClaw.isComplete } }
            .appendAction {
                armClaw.autoRotateClaw1(6.0)
                armClaw.autoRotateClaw2(17.0)
                waitFor(1.0)
            }
            .appendAction { armClaw.autoRaiseArm(-50) }
            .appendAction { waitUntil { armClaw.isComplete } }
            .appendAction {
                if (purplePixelPosition == 1 ) armClaw.openRightGrabber()
                else if (purplePixelPosition == 0) armClaw.openLeftGrabber()
                waitFor(1.0)
                armClaw.servoPickupPos()
            }
            .appendAction { armClaw.autoRaiseArm(100) }
            .appendAction { waitUntil { armClaw.isComplete } }
            .appendAction {
                var rotation = 0.deg
                if (spot == 1) rotation = 90.deg
                else if (spot == 3) rotation = (-90).deg
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                when (spot) {
                    1 -> b.strafeRight(4.0)
                    3 -> b.strafeLeft(4.0)
                }
                if (spot == 2 && teamColor == 0) b.strafeRight(2.0)
                else if (spot == 2 && teamColor == 1) b.strafeLeft(2.0)
                b.forward(6.0)
                b.turn(Math.toRadians(180.0) - rotation.rad)
                b.lineTo(p.vec())
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendTrajectory()
        return builder1
    }

    protected fun buildBackdrop(
        builder: TrajectorySequenceBuilder,
        drivetrain: AutonomousDrivetrain,
        segment: AutoAutoPathSegment,
        yellowPixelPosition: Int = 1
    ): TrajectorySequenceBuilder {
        var builder1 = builder
        builder1 = builder1.completeTrajectory()
            .appendAction {
                armClaw.autoRaiseArm(175)
                armClaw.autoRotateClaw1(3.5)
                armClaw.autoRotateClaw2(18.5)
            }
            .appendAction { waitUntil { armClaw.isComplete } }
            .appendAction {
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                when (spot) {
                    1 -> b.strafeLeft(6.0)
                    2 -> b.strafeLeft(2.0)
                    3 -> b.strafeRight(6.0)
                }
                b.turn(180.deg.rad)
                b.back(6.0)
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendAction {
                if (yellowPixelPosition == 1) armClaw.openRightGrabber()
                else armClaw.openLeftGrabber()
                waitFor(0.5)
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                try {
                    b.forward(6.0)
                    b.lineToLinearHeading(Pose2d(segment.endPosition.x, segment.endPosition.y, 0.0))
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
                armClaw.servoPickupPos()
            }
            .appendTrajectory()
        return builder1
    }

    override fun run() {
        if (spot == 0) spot = visionProcessor.spot
        else runner.run()
    }

    protected fun setupFeatures(teamColor: TeamColor) {
        armClaw = ArmClaw()
        visionProcessor = VisionProcessingFeature(SpikeMarkDetectionPipeline())
        armClaw.auto = true

        visionProcessor.setTeamColor(teamColor)
        visionProcessor.setDebugEnabled(false)
        registerFeature(armClaw)
        registerFeature(visionProcessor)
        with(armClaw) {
            blockHumanClawReleaseControl()
            blockHumanClawRotationControl()
            blockHumanArmControlForced()
            blockHumanIntakeControl()
            closeLeftGrabber()
            closeRightGrabber()
        }
    }

    protected fun setupFeatures(teamColor: Int) {
        setupFeatures(if (teamColor == 0) TeamColor.BLUE else TeamColor.RED)
    }
}
