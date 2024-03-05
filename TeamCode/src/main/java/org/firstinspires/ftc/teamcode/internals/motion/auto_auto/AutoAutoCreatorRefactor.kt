package org.firstinspires.ftc.teamcode.internals.motion.auto_auto
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature
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
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.absoluteValue

@Disabled() // WIP
class AutoAutoCreatorRefactor : OperationMode(), AutonomousOperation {
    private lateinit var config: AutoAutoCreatorConfig //changed this to lateinit
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
    private lateinit var armClaw: ArmClaw //changed this to lateinit
    private lateinit var visionProcessor: VisionProcessingFeature//changed this to lateinit
    private var spot = 0

    private lateinit var drivetrain: AutonomousDrivetrain

    private val pois = ArrayList<Vector2d>()


    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java //why??
    }

    override fun construct() {
        /* Init Variables */
        armClaw = ArmClaw().apply { auto = true }
        visionProcessor = VisionProcessingFeature(SpikeMarkDetectionPipeline()) //does this need to be defined before config?

        timer = Clock.make(UUID.randomUUID().toString())
        config = AutoAutoCreatorConfig().apply { this.askQuestions() }.takeIf { it.isValid }
            ?: throw RuntimeException("Invalid auto auto config")
        AutoNoNavigationZones.addCenterstageDefaults()

        /* Possible Configurations */
        // Colors: Blue, Red
        // Starting Position: Left, Right
        // Place Spike Mark: Yes
        // Place Backdrop: Yes, No
        // Backdrop Position: Left, Middle, Right
        // Park Place: Left, Middle, Right, Start

        visionProcessor.setTeamColor(
            if (config.teamColor == 0) VisionPipeline.TeamColor.BLUE
            else VisionPipeline.TeamColor.RED
        )

        registerFeature(armClaw)
        registerFeature(visionProcessor)

        armClaw.run {
            blockHumanClawReleaseControl()
            closeLeftGrabber()
            closeRightGrabber()
        }

        val (backdrop, leftPark, rightPark, middlePark) = when (config.teamColor) {
            0 -> arrayOf(this.backdrop, this.leftPark, this.rightPark, this.middlePark)
            else -> arrayOf(redBackdrop, redLeftPark, redRightPark, redMiddlePark)
        }



        if (config.placeBackdrop) pois.add(backdrop)

        pois.add(
            when (config.parkPlace) {
                0 -> leftPark
                2 -> rightPark
                1 -> middlePark
                else -> throw IllegalStateException("parkPlace but be 0, 1 or 2")
            }
        )

        val start = getStartPose()

        telemetry.isAutoClear = false

        var auto = Auto(start)
        drivetrain = auto.drivetrain

        /*Begin Sequence Building*/
        var builder = auto.begin()

        Logging.log("Calculating path...")
        Logging.update()
        val startTime = System.currentTimeMillis()
        initializeAutoAuto()

        var lastPose = start.vec()

        if (config.placeSpikeMark) {
            builder = builder.forward(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
            builder = builder.completeTrajectory()
                .appendAction(positionForPlaceSpikeMark)
                .appendAction { //drop spike mark
                    armClaw.apply {
                        autoRaiseArm(138)
                        waitUntil { isComplete }
                        autoRotateClaw1(14.7)
                        autoRotateClaw2(29.4)
                        waitFor(1.0)
                        autoRaiseArm(-50)
                        waitUntil { isComplete }
                    }
                }
                .appendAction(dropPixel)
                .appendAction(resetPos)
                .appendTrajectory()
            /* if(config.parkPlace == 3) */ builder = builder.back(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
        }

        if (config.parkPlace == 3) { completeLogging(startTime, auto, builder); return }

        builder = builder.completeTrajectory().appendTrajectory()

        for (poi in pois) {
            val path = BestPathFinder.getFastestPathToPoint(drivetrain.poseEstimate.vec(), poi, -90.0)
            var doPlaceBackdrop = config.placeBackdrop

            for (segment in path) {
                Logging.log("Adding Path Segment ${segment.javaClass.simpleName}")
                Logging.update()

                //Add path
                try {
                    builder = segment.addPathSegment(builder) //get to board
                    lastPose = segment.endPosition
                } catch (ignored: Exception) {
                    emergencyStop("Failed to add ${segment.javaClass.simpleName}")
                }

                when (segment.endPosition.y.absoluteValue to segment.endPosition.x to doPlaceBackdrop) {
                    36.0 to 48.0 to true -> {
                        doPlaceBackdrop = false
                        builder = buildBackdropAndParkSeq(builder, segment)
                    }
                }
            }
        }
        completeLogging(startTime, auto, builder)
    }

    private fun completeLogging(
        startTime: Long,
        auto: Auto,
        builder: TrajectorySequenceBuilder
    ) {
        var auto1 = auto
        Logging.log("Calculated path in ${System.currentTimeMillis() - startTime} ms")
        Logging.update()

        auto1 = builder.completeTrajectory().appendAction {
            while (opModeIsActive()) {
                config.run {
                    Logging.log("Config Properties")
                    Logging.log("Team Color: ${if (teamColor == 0) "Blue" else "Red"}")
                    Logging.log("Starting Position: ${if (startingPosition == 0) "Left" else "Right"}")
                    Logging.log("Place Spike Mark: ${if (placeSpikeMark) "Yes" else "No"}")
                    Logging.log("Place Backdrop: ${if (placeBackdrop) "Yes" else "No"}")
                    Logging.log("Backdrop Position: ${if (backdropPixelPosition == 0) "Left" else if (backdropPixelPosition == 1) "Middle" else "Right"}")
                    Logging.log("Park Place: ${if (parkPlace == 0) "Left" else if (parkPlace == 1) "Middle" else "Right"}")
                    Logging.log("--------------------------------")
                    Logging.log("Spot: $spot")
                    Logging.update()
                }
            }
        }.complete()

        telemetry.isAutoClear = true

        runner = AutoRunner(auto1, drivetrain, null, null, null)
    }

    override fun run() {
        if (spot == 0) spot = visionProcessor.spot
        else runner.run()
    }

    private fun buildBackdropAndParkSeq(builder: TrajectorySequenceBuilder, currentSegment: AutoAutoPathSegment): TrajectorySequenceBuilder {
        return builder.completeTrajectory()
            .appendAction {
                armClaw.apply {
                    autoRaiseArm(300)
                    autoRotateClaw1(3.5)
                    autoRotateClaw2(18.5)
                    waitUntil { isComplete }
                }
            }
            .appendAction(dropPixelOnBackdrop)
            .appendAction(park(currentSegment))
            .appendTrajectory()
    }

    private fun park(currentSegment: AutoAutoPathSegment) = Runnable {
        //park
        val pose = drivetrain.poseEstimate
        val drivetrainSeq = drivetrain.trajectorySequenceBuilder(pose)

        drivetrainSeq.apply {
            forward(6.0)
            val endPose = Pose2d(currentSegment.endPosition.x, currentSegment.endPosition.y, 0.0)
            lineToLinearHeading(endPose)
            executeSequence(this)
        }
        armClaw.servoPickupPos()
    }

    private val dropPixelOnBackdrop = Runnable {
        val pose = drivetrain.poseEstimate
        val drivetrainSeq = drivetrain.trajectorySequenceBuilder(pose)
        var farDist = 9.0

        when (spot to config.teamColor) {
            1 to 1 -> farDist = 9.0
            1 to 0 -> farDist = 9.0
            3 to 0 -> farDist = 9.0
            3 to 1 -> farDist = 9.0
        }

        //position
        drivetrainSeq.apply {
            when (spot) {
                1 -> strafeLeft(farDist)
                2 -> strafeLeft(2.0)
                3 -> strafeRight(farDist)
            }
            turn(180.deg.rad)
            back(6.0)
            executeSequence(this)
        }

        //drop
        armClaw.apply {
            when (config.backdropPixelPosition) {
                0 -> openRightGrabber()
                else -> openLeftGrabber()
            }
            waitFor(0.5)
        }
    }

    private fun getStartPose(): Pose2d {
        val (y, rot) = when (config.teamColor) {
            0 -> AutoAutoPathSegment.START_L_Y to (-90).deg.rad
            else -> -AutoAutoPathSegment.START_L_Y to 90.deg.rad
        }

        val x = when {
            (config.teamColor == 1) xor (config.startingPosition == 0) -> AutoAutoPathSegment.START_L_X
            else -> -36.0
        }
        return Pose2d(x, y, rot)
    }

    private fun executeSequence(seq: TrajectorySequenceBuilder) {
        try {
            drivetrain.followTrajectorySequenceAsync(seq.completeTrajectory())
            while(drivetrain.isBusy && opMode!!.opModeIsActive())
                drivetrain.update()
        } finally {
        }
    }

    private val positionForPlaceSpikeMark = Runnable {
        armClaw.closeRightGrabber()
        armClaw.closeLeftGrabber()

        val pose = drivetrain.poseEstimate
        val drivetrainSeq = drivetrain.trajectorySequenceBuilder(pose)

        drivetrainSeq.apply {

            val rotation = when (spot) {
                1 -> 90.deg
                2 -> 0.deg
                3 -> (-90).deg
                else -> 0.deg
            }
            when (spot) {
                1 -> forward(4.0)
                3 -> forward(7.0)
            }
            turn(180.deg.rad + rotation.rad)

            when (spot) {
                1 -> back(5.0)
                2 -> back(7.0)
                else -> back(5.0)
            }
            forward(5.0) //merge this forward into when statement below?

            when (spot){
                1 -> forward(2.0)
                3 -> forward(2.0)
            }
            executeSequence(this)
        }
    }

    private val dropPixel = Runnable {
        var backdropPixelPos = config.backdropPixelPosition
        if (config.placeBackdrop) backdropPixelPos = 1 //we want right pixel on backdrop

        when (backdropPixelPos) {
            1 -> armClaw.openRightGrabber()
            0 -> armClaw.openLeftGrabber()
        }
        waitFor(1.0)
        armClaw.servoPickupPos() //reset arm position
    }

    //not sure if this is named correctly
    private val resetPos = Runnable {
        val pose = drivetrain.poseEstimate
        val drivetrainSeq = drivetrain.trajectorySequenceBuilder(pose)

        drivetrainSeq.apply {
            val rotation = when (spot) {
                1 -> 90.deg
                3 -> (-90).deg
                2 -> (-270).deg
                else -> 0.deg
            }

            when (spot) {
                1 -> strafeRight(4.0)
                2 -> if (config.teamColor == 0) strafeRight(2.0)
                3 -> strafeLeft(4.0)
            }
            forward(6.0)
            turn(180.deg.rad - rotation.rad)
            //if(config.parkPlace == 3) lineTo(pose.vec()) //no need to return to where we dropped the pixel from
            lineTo(pose.vec())

            executeSequence(this)
        }
    }
}