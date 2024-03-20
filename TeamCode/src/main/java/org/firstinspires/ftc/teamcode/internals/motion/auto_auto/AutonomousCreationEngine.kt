package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config.AutonomousCreationEngineConfig
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config.AutonomousCreationEngineConfig.*
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BestPathFinder
import org.firstinspires.ftc.teamcode.internals.motion.initializeAutoAuto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot
import java.util.UUID

class AutonomousCreationEngine : CenterstageAutonomous() {
    private var delay = 0.0
    private val timerName = UUID.randomUUID().toString()
    private val timer: Timer
        get() {
            return Clock.get(timerName)
        }
    private lateinit var auto: Auto
    private val pois = mutableListOf<Vector2d>()
    private lateinit var config: AutonomousCreationEngineConfig
    private var currentPos = Vector2d(0.0, 0.0)
    private lateinit var drivetrain: AutonomousDrivetrain
    private lateinit var pointsForActions: MutableMap<AutonomousAction, Vector2d>
    private var actionsIndex = 0;
    private var firstRun = false
    private val reportGenerator = AutonomousReportGenerator(timerName)

    init {
        Clock.make(timerName)
    }

    override fun construct() {
        initializeConfig()
        getStartPosition(config.teamColor!!.ordinal, config.startPosition!!.ordinal)
        setupFeatures(config.teamColor!!.ordinal)
        setupPOIs()
        buildPath()
    }

    private fun initializeConfig() {
        config = AutonomousCreationEngineConfig()
        config.askQuestions()
    }

    private fun setupPOIs() {
        pointsForActions = mutableMapOf()
        with(pointsForActions) {
            if (config.teamColor!! == TeamColor.BLUE) {
                put(AutonomousAction.PARK_LEFT, leftPark)
                put(AutonomousAction.PARK_CENTER, middlePark)
                put(AutonomousAction.PARK_RIGHT, rightPark)
                put(AutonomousAction.SPIKE_MARK_SCORE, start.vec())
                put(AutonomousAction.BACKDROP_SCORE, backdrop)
            } else {
                put(AutonomousAction.PARK_LEFT, redLeftPark)
                put(AutonomousAction.PARK_CENTER, redMiddlePark)
                put(AutonomousAction.PARK_RIGHT, redRightPark)
                put(AutonomousAction.SPIKE_MARK_SCORE, start.vec())
                put(AutonomousAction.BACKDROP_SCORE, redBackdrop)
            }
        }

        for (autoAction in config.autoActions!!) {
            if (!autoAction.name.lowercase().contains("delay")) {
                pois.add(pointsForActions[autoAction]!!)
                if (autoAction == AutonomousAction.SPIKE_MARK_SCORE)
                    pois.add(start.vec())
            }
        }
    }

    private fun buildPath() {
        telemetry.isAutoClear = false

        auto = Auto(start)
        val drivetrain = auto.drivetrain

        var builder = auto.begin()

        Logging.log("Calculating path...")
        Logging.update()
        val startT = System.currentTimeMillis()
        initializeAutoAuto()
        var last = start.vec()

        for (poi in pois) {
            if (poi != last) {
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
                }
            }
            if (actionsIndex < config.autoActions!!.size) {
                if (isPointAnAction(last).contains(config.autoActions!![actionsIndex])) {
                    when (config.autoActions!![actionsIndex]) {
                        AutonomousAction.SPIKE_MARK_SCORE -> {
                            builder.forward(DISTANCE_TO_SPIKE_MARK)
                            builder = buildSpikeMarkIntakeMethod(builder, drivetrain, config.teamColor!!.ordinal)
                            builder.back(DISTANCE_TO_SPIKE_MARK)
                            builder = builder.completeTrajectory().appendTrajectory()
                        }
                        AutonomousAction.BACKDROP_SCORE -> {
                            builder = buildBackdrop(builder, drivetrain, poi)
                        }
                        AutonomousAction.DELAY_1S -> delay += 1
                        AutonomousAction.DELAY_5S -> delay += 5
                        else -> continue
                    }
                    actionsIndex++
                }
            }
        }

        Logging.log("Calculated path in " + (System.currentTimeMillis() - startT) + "ms")
        Logging.update()

        auto = builder.completeTrajectory().appendAction {
            reportGenerator.markTime("End Autonomous")
            while (opModeIsActive()) {
                if (timer.elapsed(30.0))  {
                    Logging.log(reportGenerator.generateReport())
                    Logging.log(config.toString())
                    Logging.update()
                }
            }
        }.complete()
        telemetry.isAutoClear = true

        runner = AutoRunner(auto, drivetrain, null, null, null)

        Logging.log("Current Config:")
        Logging.log(config.toString())
        Logging.update()
    }

    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java
    }

    override fun run() {
        if (firstRun) {
            timer.reset()
            if (delay > 0) {
                waitUntil { timer.elapsed(delay) }
            }
            firstRun = false
        }
        super.run()
        currentPos = drivetrain.poseEstimate.vec()
    }

    private fun isPointAnAction(p: Vector2d): List<AutonomousAction> {
        val actions = mutableListOf<AutonomousAction>()
        for (entry in pointsForActions) {
            if (p == entry.value) actions.add(entry.key)
        }
        return actions
    }
}