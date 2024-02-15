package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.features.ArmClaw
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature
import org.firstinspires.ftc.teamcode.internals.documentation.ButtonUsage
import org.firstinspires.ftc.teamcode.internals.documentation.ReferToButtonUsage
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.image.VisionPipeline
import org.firstinspires.ftc.teamcode.internals.image.centerstage.SpikeMarkDetectionPipeline
import org.firstinspires.ftc.teamcode.internals.math.units.deg
import org.firstinspires.ftc.teamcode.internals.motion.initializeAutoAuto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class AutoAutoCreatorV2 : OperationMode(), AutonomousOperation {
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
        TODO("Not yet implemented")
    }

    override fun construct() {
        TODO("Not yet implemented")
    }

    override fun run() {
        TODO("Not yet implemented")
    }

}