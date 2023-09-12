package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller2
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode
import org.firstinspires.ftc.teamcode.internals.misc.MecanumDriver
import kotlin.math.abs

/**
* A mecanum drivetrain&mdash;this doesn't require odometry, hence it being "native" by not relying on external libraries. If you have odometry, use MecanumDrivetrain
 * @param drivetrainMapMode The motor layout of the drivetrain.
 * @param useExpansionHub Whether or not to use the expansion hub to get the motors. Requires that the expansion hub motors be initialized before use
 * @param fieldCentric Whether or not to use field centric controls. The imu must be initialized prior to use.
 * @param isRotInverted Whether or not to invert the rotation controls. Required for our second robot.
 * @param inputDampening Whether or not to smooth the acceleration of the motors
 */
class NativeMecanumDrivetrain(
    private var drivetrainMapMode: DrivetrainMapMode,
    private var useExpansionHub: Boolean,
    private var fieldCentric: Boolean,
    private var isRotInverted: Boolean,
    private var inputDampening: Boolean,
) : Feature(),
    Buildable {

    private var mecanumDriver: MecanumDriver? = null


    private var previousX: Double = 0.0
    private var previousY: Double = 0.0
    private var previousRot: Double = 0.0
    private val dampeningFactor = 0.5 // Between 0 and 1, tests have shown 0.5 to be a sweet spot
    private var speedMultiplier = 0.5

    /*
    The following constants dictate the power each controller sends
    for both coordinate and rotational motion. These should be used
    to optimise the driver expereince.
     */
    var CONTROL1_COORDINATE_MOTION = 0.65
    var CONTROL2_COORDINATE_MOTION = 0.23
    var CONTROL1_ROTATIONAL_MOTION = 0.65
    var CONTROL2_ROTATIONAL_MOTION = 0.26

    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode, useExpansionHub: Boolean) : this(
        drivetrainMapMode,
        useExpansionHub,
        false,
        false,
        false
    )

    override fun build() {

        mecanumDriver = MecanumDriver(drivetrainMapMode, useExpansionHub, fieldCentric)
    }

    override fun loop() {
        /*
        Right joystick controlls rotation
        Left joystick controlls forward/backward/left/right movement

        Controller 1 has full power
        Controller 2 has reduced power (for finer control)
         */

        var rot: Double = if (!isRotInverted) {
            CONTROL1_ROTATIONAL_MOTION * controller1.rightStickX + CONTROL2_ROTATIONAL_MOTION * controller2.rightStickX
        } else {
            -(CONTROL1_ROTATIONAL_MOTION * controller1.rightStickX + CONTROL2_ROTATIONAL_MOTION * controller2.rightStickX)
        }

        var x: Double = -1 * (CONTROL1_COORDINATE_MOTION * controller1.leftStickX + CONTROL2_COORDINATE_MOTION * controller2.leftStickX)
        var y: Double = (CONTROL1_COORDINATE_MOTION * controller1.leftStickY + CONTROL2_COORDINATE_MOTION * controller2.leftStickY)

        // Apply Dampening (if enabled)
        if (inputDampening) {
            x = dampenInput(x, previousX)
            y = dampenInput(y, previousY)
            rot = dampenInput(rot, previousRot)
            previousX = x
            previousY = y
            previousRot = rot
        }

        mecanumDriver!!.runMecanum(x * speedMultiplier, y * speedMultiplier, rot * speedMultiplier)
    }

    private fun dampenInput(x: Double, prev: Double): Double {
        val step = abs(x - prev) * dampeningFactor
        return  if          (x > prev)  prev + step
                else if     (x < prev)  prev - step
                else                    x
    }
}
