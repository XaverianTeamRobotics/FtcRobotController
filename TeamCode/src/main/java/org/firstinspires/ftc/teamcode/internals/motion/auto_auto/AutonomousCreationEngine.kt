package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config.AutonomousCreationEngineConfig
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BestPathFinder
import org.firstinspires.ftc.teamcode.internals.motion.initializeAutoAuto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot

class AutonomousCreationEngine : CenterstageAutonomous() {
    private lateinit var auto: Auto
    private val pois = mutableListOf<Vector2d>()
    private lateinit var start: Pose2d
    private lateinit var config: AutonomousCreationEngineConfig
    private var currentPos = Vector2d(0.0, 0.0)
    private lateinit var drivetrain: AutonomousDrivetrain

    override fun construct() {
        initializeConfig()
        setupFeatures(config.teamColor!!.ordinal)
        setupPOIs()
        getStartPosition()
        buildPath()
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
        Logging.log("Calculated path in " + (System.currentTimeMillis() - startT) + "ms")
        Logging.update()

        auto = builder.completeTrajectory().appendAction { while (opModeIsActive()) { } }.complete()
        telemetry.isAutoClear = true

        runner = AutoRunner(auto, drivetrain, null, null, null)
    }

    private fun getStartPosition() {
        TODO("Not yet implemented")
    }

    private fun setupPOIs() {
        TODO("Not yet implemented")
    }

    private fun initializeConfig() {
        TODO("Not yet implemented")
    }

    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java
    }

    override fun run() {
        super.run()
        currentPos = drivetrain.poseEstimate.vec()
    }
}