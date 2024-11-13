package org.firstinspires.ftc.teamcode.autonomous.localization.localizers

import org.firstinspires.ftc.teamcode.autonomous.localization.RobotPose
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.pinpoint
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.internals.units.LengthUnit

/**
 * A localizer that uses the GoBilda Pinpoint system to determine the pose of the robot.
 * @param offsetX the x offset of the X odometer of the Pinpoint system from the center of the robot
 * @param offsetY the y offset of the Y odometer of the Pinpoint system from the center of the robot
 * @param odoType the type of odometry pods used in the GoBilda Pinpoint system
 * @param directionX the direction of the encoder on the x axis. Forward is increasing encoder values when the robot moves forward
 * @param directionY the direction of the encoder on the y axis. Forward is increasing encoder values when the robot moves left
 */
class GoBildaPinpointLocalizer(offsetX: LengthUnit, offsetY: LengthUnit,
                               odoType: GoBildaPinpointDriver.GoBildaOdometryPods = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD,
                               directionX: GoBildaPinpointDriver.EncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD,
                               directionY: GoBildaPinpointDriver.EncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD
): Localizer {
    init {
        try {
            pinpoint.manufacturer
        } catch (e: Exception) {
            throw Exception("GoBilda Pinpoint not found")
        }

        pinpoint.setOffsets(offsetX.mm, offsetY.mm)
        pinpoint.setEncoderResolution(odoType)
        pinpoint.setEncoderDirections(directionX, directionY)
        pinpoint.resetPosAndIMU()
        // Wait 0.25 seconds for the IMU to calibrate
        // The wait may not be perfect, but it's an approximation.
        // Because this should take place during init, we should in theory be fine
        Thread.sleep(250)
    }

    override var pose: RobotPose?
        get() {
            pinpoint.update()
            return pinpoint.position
        }
        set(value) {
            pinpoint.setPosition(value!!)
        }

    override val velocity: RobotPose?
        get() {
            pinpoint.update()
            return pinpoint.velocity
        }
}