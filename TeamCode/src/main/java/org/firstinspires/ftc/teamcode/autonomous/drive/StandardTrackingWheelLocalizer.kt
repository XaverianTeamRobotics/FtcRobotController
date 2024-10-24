package org.firstinspires.ftc.teamcode.autonomous.drive

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.autonomous.util.Encoder
import java.util.Arrays

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
class StandardTrackingWheelLocalizer(
    hardwareMap: HardwareMap,
    lastTrackingEncPositions: MutableList<Int?>,
    lastTrackingEncVels: MutableList<Int?>
) : ThreeTrackingWheelLocalizer(
    listOf<Pose2d>(
        Pose2d(0.0, LATERAL_DISTANCE / 2, 0.0),  // left
        Pose2d(0.0, -LATERAL_DISTANCE / 2, 0.0),  // right
        Pose2d(FORWARD_OFFSET, 0.0, Math.toRadians(90.0)) // front
    )
) {
    private val leftEncoder: Encoder
    private val rightEncoder: Encoder
    private val frontEncoder: Encoder

    private val lastEncPositions: MutableList<Int?>
    private val lastEncVels: MutableList<Int?>

    init {
        lastEncPositions = lastTrackingEncPositions
        lastEncVels = lastTrackingEncVels

        leftEncoder = Encoder(hardwareMap.get<DcMotorEx?>(DcMotorEx::class.java, "leftEncoder"))
        rightEncoder = Encoder(hardwareMap.get<DcMotorEx?>(DcMotorEx::class.java, "rightEncoder"))
        frontEncoder = Encoder(hardwareMap.get<DcMotorEx?>(DcMotorEx::class.java, "frontEncoder"))

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
    }

    override fun getWheelPositions(): List<Double> {
        val leftPos = leftEncoder.getCurrentPosition()
        val rightPos = rightEncoder.getCurrentPosition()
        val frontPos = frontEncoder.getCurrentPosition()

        lastEncPositions.clear()
        lastEncPositions.add(leftPos)
        lastEncPositions.add(rightPos)
        lastEncPositions.add(frontPos)

        return Arrays.asList<Double?>(
            encoderTicksToInches(leftPos.toDouble()),
            encoderTicksToInches(rightPos.toDouble()),
            encoderTicksToInches(frontPos.toDouble())
        )
    }

    public override fun getWheelVelocities(): List<Double> {
        val leftVel = leftEncoder.getCorrectedVelocity().toInt()
        val rightVel = rightEncoder.getCorrectedVelocity().toInt()
        val frontVel = frontEncoder.getCorrectedVelocity().toInt()

        lastEncVels.clear()
        lastEncVels.add(leftVel)
        lastEncVels.add(rightVel)
        lastEncVels.add(frontVel)

        return listOf<Double>(
            encoderTicksToInches(leftVel.toDouble()),
            encoderTicksToInches(rightVel.toDouble()),
            encoderTicksToInches(frontVel.toDouble())
        )
    }

    companion object {
        @JvmField
        var TICKS_PER_REV: Double = 0.0
        @JvmField
        var WHEEL_RADIUS: Double = 2.0 // in
        @JvmField
        var GEAR_RATIO: Double = 1.0 // output (wheel) speed / input (encoder) speed

        @JvmField
        var LATERAL_DISTANCE: Double = 10.0 // in; distance between the left and right wheels
        @JvmField
        var FORWARD_OFFSET: Double = 4.0 // in; offset of the lateral wheel

        fun encoderTicksToInches(ticks: Double): Double {
            return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV
        }

        fun rpmToVelocity(rpm: Double): Double {
            return rpm * GEAR_RATIO * 2.0 * Math.PI * WHEEL_RADIUS / 60.0
        }
    }
}
