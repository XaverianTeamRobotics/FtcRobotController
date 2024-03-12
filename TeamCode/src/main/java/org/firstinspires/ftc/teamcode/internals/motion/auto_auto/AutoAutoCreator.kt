package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.image.VisionPipeline
import org.firstinspires.ftc.teamcode.internals.image.centerstage.SpikeMarkDetectionPipeline
import org.firstinspires.ftc.teamcode.internals.math.units.deg
import org.firstinspires.ftc.teamcode.internals.motion.initializeAutoAuto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot
import java.util.*

@ReferToButtonUsage("AutoAutoCreatorConfig")
class AutoAutoCreator : OperationMode(), AutonomousOperation {
    private var config: AutoAutoCreatorConfig? = null
    lateinit var timer: Timer
    lateinit var runner: AutoRunner

    private val backdrop = Vector2d(48.00, 36.00)
    private val spikeMark = Vector2d(12.00, 36.00)
    private val spikeMark2 = Vector2d(-36.00, 36.00)
    private val leftPark = Vector2d(60.00, 60.00)
    private val rightPark = Vector2d(60.00, 12.00)
    private val middlePark = Vector2d(48.00, 36.00)

    private val redBackdrop = Vector2d(backdrop.x, -backdrop.y)
    private val redLeftPark = Vector2d(rightPark.x, -rightPark.y)
    private val redRightPark = Vector2d(leftPark.x, -leftPark.y)
    private val redMiddlePark = Vector2d(middlePark.x, -middlePark.y)
    private var armClaw: ArmClaw? = null
    private var visionProcessor: VisionProcessingFeature? = null
    private var spot = 0

    private val pois = ArrayList<Vector2d>()

    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java
    }

    override fun construct() {
        armClaw = ArmClaw()
        visionProcessor = VisionProcessingFeature(SpikeMarkDetectionPipeline())

        armClaw!!.auto = true

        timer = Clock.make(UUID.randomUUID().toString())
        config = AutoAutoCreatorConfig()
        config!!.askQuestions()
        if (!config!!.isValid) throw RuntimeException("Invalid auto auto config")
        AutoNoNavigationZones.addCenterstageDefaults()

        visionProcessor!!.setTeamColor(if (config!!.teamColor == 0) VisionPipeline.TeamColor.BLUE else VisionPipeline.TeamColor.RED)
        registerFeature(armClaw!!)
        registerFeature(visionProcessor!!)
        with (armClaw!!) {
            blockHumanClawReleaseControl()
            blockHumanClawRotationControl()
            blockHumanArmControlForced()
            blockHumanIntakeControl()
            closeLeftGrabber()
            closeRightGrabber()
        }

        // Change the values based on the team color
        val backdrop = if (config!!.teamColor == 0) this.backdrop else redBackdrop
        val leftPark = if (config!!.teamColor == 0) this.leftPark else redLeftPark
        val rightPark = if (config!!.teamColor == 0) this.rightPark else redRightPark
        val middlePark = if (config!!.teamColor == 0) this.middlePark else redMiddlePark

        if (config!!.placeBackdrop) pois.add(backdrop)

        when (config!!.parkPlace) {
            0 -> pois.add(leftPark)
            2 -> pois.add(rightPark)
            1 -> pois.add(middlePark)
        }

        val y = (if (config!!.teamColor == 0) 1 else -1) * AutoAutoPathSegment.START_L_Y
        val rot = if (config!!.teamColor == 0) Math.toRadians(-90.00) else Math.toRadians(90.00)
        var xStartingPos = config!!.startingPosition == 0
        if (config!!.teamColor == 1) xStartingPos = !xStartingPos
        val x = if (xStartingPos) AutoAutoPathSegment.START_L_X else -36.0
        val start = Pose2d(x, y, rot)

        telemetry.isAutoClear = false

        var auto = Auto(start)
        val drivetrain = auto.drivetrain

        var builder = auto.begin()

        Logging.log("Calculating path...")
        Logging.update()
        val startT = System.currentTimeMillis()
        initializeAutoAuto()
        var last = start.vec()

        if (config!!.placeSpikeMark) {
            builder = buildSpikeMark(builder, drivetrain)
            if (config!!.parkPlace != 3) builder = builder.completeTrajectory().appendTrajectory()
        }

        if (config!!.parkPlace != 3) {
            var needToScore = config!!.placeBackdrop
            for (poi in pois) {
                val path = BestPathFinder.getFastestPathToPoint(last, poi, 0.0)
                for (segment in path) {
                    Logging.log("Adding Path Segment " + segment.javaClass.simpleName)
                    Logging.update()
                    try {
                        builder = segment.addPathSegment(builder)
                        last = segment.endPosition
                    } catch (ignored: Exception) {
                        emergencyStop("Failed to add " + segment.javaClass.simpleName)
                    }

                    if ((segment.endPosition.y == 36.00 || segment.endPosition.y == -36.00)
                        && (segment.endPosition.x == 48.00
                                ) && needToScore
                    ) {
                        needToScore = false
                        builder = buildBackdrop(builder, drivetrain, segment)
                    }
                }
            }
        }
        Logging.log("Calculated path in " + (System.currentTimeMillis() - startT) + "ms")
        Logging.update()

        auto = builder.completeTrajectory().appendAction {
            while (opModeIsActive()) {
                Logging.log("Config Properties")
                Logging.log("Team Color: ${if (config!!.teamColor == 0) "Blue" else "Red"}")
                Logging.log("Starting Position: ${if (config!!.startingPosition == 0) "Left" else "Right"}")
                Logging.log("Place Spike Mark: ${if (config!!.placeSpikeMark) "Yes" else "No"}")
                Logging.log("Place Backdrop: ${if (config!!.placeBackdrop) "Yes" else "No"}")
                Logging.log("Yellow Pixel Position: ${if (config!!.yellowPixelPosition == 0) "Left" else if (config!!.yellowPixelPosition == 1) "Middle" else "Right"}")
                Logging.log("Park Place: ${if (config!!.parkPlace == 0) "Left" else if (config!!.parkPlace == 1) "Middle" else "Right"}")
                Logging.log("--------------------------------")
                Logging.log("Spot: $spot")
                Logging.update()
            }
        }.complete()

        telemetry.isAutoClear = true

        runner = AutoRunner(auto, drivetrain, null, null, null)
    }

    private fun buildSpikeMark(builder: TrajectorySequenceBuilder, drivetrain: AutonomousDrivetrain): TrajectorySequenceBuilder {
        var builder1 = builder
        builder1 = builder1.forward(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
        builder1 = builder1.completeTrajectory()
            .appendAction {
                armClaw!!.closeRightGrabber()
                armClaw!!.closeLeftGrabber()

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
                if (spot == 3 && config!!.teamColor == 1) b.back(5.0)
                else if (spot == 2) b.back(5.5)
                else b.back(5.0)
                b.forward(5.0)
                if (spot == 3 || spot == 1) b.forward(2.0)
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opMode!!.opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendAction { armClaw!!.autoRaiseArm(138) }
            .appendAction { waitUntil { armClaw!!.isComplete } }
            .appendAction {
                armClaw!!.autoRotateClaw1(6.0)
                armClaw!!.autoRotateClaw2(17.0)
                waitFor(1.0)
            }
            .appendAction { armClaw!!.autoRaiseArm(-50) }
            .appendAction { waitUntil { armClaw!!.isComplete } }
            .appendAction {
                if (config!!.yellowPixelPosition == 0 || !config!!.placeBackdrop) armClaw!!.openRightGrabber()
                else if (config!!.yellowPixelPosition == 1) armClaw!!.openLeftGrabber()
                waitFor(1.0)
                armClaw!!.servoPickupPos()
            }
            .appendAction { armClaw!!.autoRaiseArm(100) }
            .appendAction { waitUntil { armClaw!!.isComplete } }
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
                if (spot == 2 && config!!.teamColor == 0) b.strafeRight(2.0)
                else if (spot == 2 && config!!.teamColor == 1) b.strafeLeft(2.0)
                b.forward(6.0)
                b.turn(Math.toRadians(180.0) - rotation.rad)
                b.lineTo(p.vec())
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opMode!!.opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendTrajectory()
        builder1 = builder1.back(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
        return builder1
    }

    private fun buildBackdrop(builder: TrajectorySequenceBuilder, drivetrain: AutonomousDrivetrain, segment: AutoAutoPathSegment): TrajectorySequenceBuilder {
        var builder1 = builder
        builder1 = builder1.completeTrajectory()
            .appendAction {
                armClaw!!.autoRaiseArm(175)
                armClaw!!.autoRotateClaw1(3.5)
                armClaw!!.autoRotateClaw2(18.5)
            }
            .appendAction { waitUntil { armClaw!!.isComplete } }
            .appendAction {
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                var farDist = 9.0
                if ((spot == 1 && config!!.teamColor == 1) || (spot == 3 && config!!.teamColor == 0) || (spot == 3 && config!!.teamColor == 1) || (spot == 1 && config!!.teamColor == 0))
                    farDist = 6.0
                when (spot) {
                    1 -> b.strafeLeft(farDist)
                    2 -> b.strafeLeft(2.0)
                    3 -> b.strafeRight(farDist)
                }
                b.turn(180.deg.rad)
                b.back(6.0)
                try {
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opMode!!.opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
            }
            .appendAction {
                if (config!!.yellowPixelPosition == 1) armClaw!!.openRightGrabber()
                else armClaw!!.openLeftGrabber()
                waitFor(0.5)
                val p = drivetrain.poseEstimate
                val b = drivetrain.trajectorySequenceBuilder(p)
                try {
                    b.forward(6.0)
                    b.lineToLinearHeading(Pose2d(segment.endPosition.x, segment.endPosition.y, 0.0))
                    drivetrain.followTrajectorySequenceAsync(b.completeTrajectory())
                    while (drivetrain.isBusy && opMode!!.opModeIsActive()) {
                        drivetrain.update()
                    }
                } catch (_: Exception) {
                }
                armClaw!!.servoPickupPos()
            }
            .appendTrajectory()
        return builder1
    }

    override fun run() {
        if (spot == 0) spot = visionProcessor!!.spot
        else runner.run()
    }
}
