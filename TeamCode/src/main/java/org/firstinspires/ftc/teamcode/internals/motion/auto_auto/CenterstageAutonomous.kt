package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
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
    protected lateinit var start: Pose2d

    protected val backdrop = Vector2d(48.00, 36.00)
    protected val spikeMark = Vector2d(12.00, 36.00)
    protected val spikeMark2 = Vector2d(-36.00, 36.00)
    protected val leftPark = Vector2d(60.00, 60.00)
    protected val rightPark = Vector2d(60.00, 12.00)
    protected val middlePark = Vector2d(48.00, 36.00)

    protected val redBackdrop = Vector2d(backdrop.x, -backdrop.y)
    protected val redLeftPark = Vector2d(rightPark.x, -rightPark.y)
    protected val redRightPark = Vector2d(leftPark.x, -leftPark.y)
    protected val redMiddlePark = Vector2d(middlePark.x, -middlePark.y)
    protected val redSpikeMark = Vector2d(spikeMark2.x, -spikeMark2.y)
    protected val redSpikeMark2 = Vector2d(spikeMark.x, -spikeMark.y)

    protected fun buildSpikeMarkArmMethod(
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

    protected fun buildSpikeMarkIntakeMethod(
        builder: TrajectorySequenceBuilder,
        drivetrain: AutonomousDrivetrain,
        teamColor: Int,
    ): TrajectorySequenceBuilder {
        lateinit var startPose: Pose2d
        var rotation = 0.deg
        val builder1 = builder.completeTrajectory()
            .appendAction {
                if (spot == 1) rotation = (-270).deg
                else if (spot == 3) rotation = (-90).deg
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                startPose = p
                if (spot == 1 || spot == 3) {
                    b.forward(6.0)
                }
                b.turn(Math.toRadians(180.0) + rotation.rad)
                b.back(8.0)
                b.forward(3.0)
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (e: Exception) {
                    emergencyStop(e.message)
                }
                Devices.servo0.position = 20.0
                Devices.servo1.position = 13.1
                armClaw.blockHumanIntakeControl()
                armClaw.intakeRunning = true
                waitFor(1.0)
                armClaw.intakeRunning = false

                val p2 = drivetrain.poseEstimate
                val b2 = drivetrain.trajectorySequenceBuilder(p2)
                b2.lineTo(startPose.vec())
                b2.turn(-(rotation.rad + 180.0.deg.rad))
                b2.addTemporalMarker(0.5) {
                    armClaw.servoPickupPos()
                }
                try {
                    drivetrain.followTrajectorySequenceAsync(b2.completeTrajectory())
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
        endPosition: Vector2d,
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
                    b.lineToLinearHeading(Pose2d(endPosition.x, endPosition.y, 0.0))
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

    protected fun getStartPosition(teamColor: Int, startPosition: Int) {
        val y = (if (teamColor == 0) 1 else -1) * AutoAutoPathSegment.START_L_Y
        val rot = if (teamColor == 0) Math.toRadians(-90.00) else Math.toRadians(90.00)
        var xStartingPos = startPosition == 0
        if (teamColor == 1) xStartingPos = !xStartingPos
        val x = if (xStartingPos) AutoAutoPathSegment.START_L_X else -36.0
        start = Pose2d(x, y, rot)
    }
}
