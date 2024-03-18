package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config.AutoAutoCreatorConfig
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoNoNavigationZones
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BestPathFinder
import org.firstinspires.ftc.teamcode.internals.motion.initializeAutoAuto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot
import java.util.*

@ReferToButtonUsage("AutoAutoCreatorConfig")
class AutoAutoCreator : CenterstageAutonomous() {
    private var config: AutoAutoCreatorConfig? = null
    lateinit var timer: Timer

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

    private val pois = ArrayList<Vector2d>()
    private lateinit var start: Pose2d

    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java
    }

    override fun construct() {
        getConfig()
        setupFeatures(config!!.teamColor)
        setupPOIs()
        getStartPosition()
        buildPath()
    }

     private fun buildPath() {
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
            builder.forward(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
            builder = buildSpikeMark(builder, drivetrain, config!!.teamColor, 0)
            builder.back(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK)
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
                        builder = buildBackdrop(builder, drivetrain, segment,)
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

    private fun getStartPosition() {
        val y = (if (config!!.teamColor == 0) 1 else -1) * AutoAutoPathSegment.START_L_Y
        val rot = if (config!!.teamColor == 0) Math.toRadians(-90.00) else Math.toRadians(90.00)
        var xStartingPos = config!!.startingPosition == 0
        if (config!!.teamColor == 1) xStartingPos = !xStartingPos
        val x = if (xStartingPos) AutoAutoPathSegment.START_L_X else -36.0
        start = Pose2d(x, y, rot)
    }

    private fun setupPOIs() {
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
    }

    private fun getConfig() {
        timer = Clock.make(UUID.randomUUID().toString())
        config =
            AutoAutoCreatorConfig()
        config!!.askQuestions()
        if (!config!!.isValid) throw RuntimeException("Invalid auto auto config")
        AutoNoNavigationZones.addCenterstageDefaults()
    }

}
