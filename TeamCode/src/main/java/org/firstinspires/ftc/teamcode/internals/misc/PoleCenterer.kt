package org.firstinspires.ftc.teamcode.internals.misc

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.image.powerplay.PoleNavigator
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.ImageFeedbackController
import java.util.*

@Config
class PoleCenterer {
    private var drivetrain: AutonomousDrivetrain? = null
    private val controller: ImageFeedbackController

    constructor() {
        controller = ImageFeedbackController(
            PoleNavigator(),
            kPx,
            kIx,
            kDx,
            mV,
            mA,
            sp,
            ctol,
            etol,
            kPy,
            kIy,
            kDy,
            mV,
            mA,
            sp,
            ctol,
            etol
        )
    }

    constructor(index: Int) {
        controller = ImageFeedbackController(
            PoleNavigator(index),
            kPx,
            kIx,
            kDx,
            mV,
            mA,
            sp,
            ctol,
            etol,
            kPy,
            kIy,
            kDy,
            mV,
            mA,
            sp,
            ctol,
            etol
        )
    }

    fun loop(): Boolean {
        val xy = controller.calculate()
        val input = Vector2d(
            -xy[1],
            -xy[0]
        )
        drivetrain!!.setWeightedDrivePower(
            Pose2d(
                input.x,
                input.y,
                0.0
            )
        )
        return xy[2] == 1.0
    }

    fun center() {
        if (drivetrain == null) {
            throw NullPointerException("Drivetrain can't be null!")
        }
        var done = false
        while (!done && opMode!!.opModeIsActive()) {
            done = loop()
        }
        drivetrain!!.setWeightedDrivePower(Pose2d(0.0, 0.0, 0.0))
    }

    fun setDrivetrain(drivetrain: AutonomousDrivetrain?) {
        this.drivetrain = drivetrain
    }

    companion object {
        var kPx: Double = 1.0
        var kPy: Double = 0.8
        var kIx: Double = 0.0
        var kIy: Double = 0.0
        var kDx: Double = 0.0
        var kDy: Double = 0.0
        var mV: Double = 0.1
        var mA: Double = 0.1
        var sp: Double = 0.0
        var ctol: Double = 5.0
        var etol: Double = 5.0
    }
}
