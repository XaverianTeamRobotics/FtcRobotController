package org.firstinspires.ftc.teamcode.features

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.misc.RatelimitCalc
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.SlewRateLimiter
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DashboardLogging

/**
 * A mecanum drivetrain. This relies on odometry. Use [NativeMecanumDrivetrain] if you don't have odometry.
 */
@Config
class PowerplayMecanumDrivetrain(private val FIELD_CENTRIC: Boolean, private val DRIVER_ASSIST: Boolean) : Feature(),
    Buildable {
    private var drivetrain: AutonomousDrivetrain? = null
    private val rlX = SlewRateLimiter(1.0)
    private val rlY = SlewRateLimiter(1.0)
    private val rcX = RatelimitCalc(xYMin, xYMax, pMin, pMax)
    private val rcY = RatelimitCalc(yYMin, yYMax, pMin, pMax)

    override fun build() {
        drivetrain = AutonomousDrivetrain()
        drivetrain!!.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
        drivetrain!!.poseEstimate = PoseBucket.getPose()
    }

    override fun loop() {
        // Read current pose
        val poseEstimate = drivetrain!!.poseEstimate
        // Get gamepad inputs
        var x = Devices.controller1.leftStickX * xMult
        var y = Devices.controller1.leftStickY * yMult
        val r = Devices.controller1.rightStickX * rMult
        DashboardLogging.logData("iX", x)
        DashboardLogging.logData("iY", y)
        DashboardLogging.logData("iR", r)
        val reset = Devices.controller1.touchpad
        if (reset) {
            drivetrain!!.poseEstimate = Pose2d(0.0, 0.0, 0.0)
        }
        // dampen if assist is enabled
        if (DRIVER_ASSIST) {
            val v = dampen(x / 100, y / 100)
            x = Range.clip(v[0] * 100, -100.0, 100.0)
            y = Range.clip(v[1] * 100, -100.0, 100.0)
        }
        if (!simulated) {
            // Create a vector from the gamepad x/y inputs
            // Then, rotate that vector by the inverse of that heading if we're using field centric--otherwise we'll just assume the heading is 0
            val input = if (FIELD_CENTRIC) {
                Vector2d(
                    -Compressor.compress(y),
                    -Compressor.compress(x)
                ).rotated(-poseEstimate.heading)
            } else {
                Vector2d(
                    -Compressor.compress(y),
                    -Compressor.compress(x)
                )
            }
            // Pass in the rotated input + right stick value for rotation
            // Rotation is not part of the rotated input thus must be passed in separately
            drivetrain!!.setWeightedDrivePower(
                Pose2d(
                    input.x,
                    input.y,
                    -Compressor.compress(r)
                )
            )
        }
        // Update everything. Odometry. Etc.
        DashboardLogging.logData("oX", x)
        DashboardLogging.logData("oY", y)
        DashboardLogging.logData("oR", r)
        DashboardLogging.update()
        drivetrain!!.update()
    }

    private fun dampen(x: Double, y: Double): DoubleArray {
        val pos = Devices.encoder5.position
        return doubleArrayOf(limit(x, pos, rlX, rcX), limit(y, pos, rlY, rcY))
    }

    /**
     * Allows debug logging to both DS and Dashboard via Logging.log($logger, $rate) where $logger is the logger argument (the caption of the data) and $rate is the rate double calculated in the method.
     */
    private fun limit(
        vel: Double,
        pos: Int,
        limiter: SlewRateLimiter,
        calculator: RatelimitCalc,
        logger: String? = null
    ): Double {
        val rate = calculator.calculate(pos)
        if (logger != null) {
            DashboardLogging.log(logger, rate)
        }
        limiter.setRateLimit(rate)
        return limiter.calculate(vel)
    }

    companion object {
        var xMult: Double = 0.6
        var yMult: Double = 0.6
        var rMult: Double = 0.6
        var xYMin: Double = 7.0
        var xYMax: Double = 1.0
        var yYMin: Double = 7.0
        var yYMax: Double = 1.0
        var pMin: Double = PowerplayFourMotorArm.ArmPosition.RESET.height
        var pMax: Double = PowerplayFourMotorArm.ArmPosition.JNCT_HIGH.height
        var simulated: Boolean = false
    }
}
