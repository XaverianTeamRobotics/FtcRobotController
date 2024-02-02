package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.documentation.ButtonName
import org.firstinspires.ftc.teamcode.internals.documentation.ButtonUsage
import org.firstinspires.ftc.teamcode.internals.documentation.ControllerName
import org.firstinspires.ftc.teamcode.internals.documentation.ReferableButtonUsage
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import java.util.*
import kotlin.math.max
import kotlin.math.min

//our two devices our the motors for the arm
/**
 * this runs the grabber, intake, and claw pivot mechanism.  Dpad left is to close the left grabber,
 * Dpad right closes the right grabber, Dpad up opens both grabbers, Dpad down starts/stops the
 * intake, the bumpers rotate the claw mechanism, and the triggers move the arms.
 *
 *
 * All controls are in controller 2
 *
 *
 * The SHARE/BACK button resets the home position of the arm
 *
 *
 * The OPTIONS/START button disables auto claw rotation and the auto lift
 */
@ButtonUsage(button = ButtonName.Y, description = "Rotate claw to pickup position", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.LEFT_STICK_BUTTON, description = "Rotate claw to flat position", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.RIGHT_STICK_BUTTON, description = "Rotate claw to scoring position", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.DPAD_LEFT, description = "Toggle left grabber", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.DPAD_RIGHT, description = "Toggle right grabber", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.DPAD_UP, description = "Reverse intake (hold)", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.DPAD_DOWN, description = "Toggle intake", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.LEFT_BUMPER, description = "Pixel selector to left", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.RIGHT_BUMPER, description = "Pixel selector to right", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.X, description = "Pixel holder down", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.B, description = "Pixel holder up", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.LEFT_TRIGGER, description = "Lower arm", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.RIGHT_TRIGGER, description = "Raise arm", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.BACK, description = "Emergency stop autonomous features", controller = ControllerName.CONTROLLER_2)
@ButtonUsage(button = ButtonName.RIGHT_STICK_BUTTON, description = "Windshield wiper :)", controller = ControllerName.CONTROLLER_1)
@ReferableButtonUsage(referableAs = "ArmClaw")
class ArmClaw : Feature(), Buildable {
    private var counter = 0.0
    private var dpadPressed = false
    private var counterLeft = 0.0 //left servo
    private var counterRight = 0.0 //right servo
    private var dpadPressedLeft = false //left servo
    private var dpadPressedRight = false //right servo
    private var encoderHomePosition = 0
    private var autonomousArmControl = false
    private var autonomousIntakeControl = false
    private var autonomousClawRotationControl = false
    private var autonomousClawReleaseControl = false
    private var armTargetHeight = 0
    private var armLiftingInProgress = false

    var auto: Boolean = false

    override fun build() {
        Devices.servo3.position = R_OPEN.toDouble()
        Devices.servo2.position = L_OPEN.toDouble()
        servoPickupPos()
        Devices.motor0.power = 0.0
        encoderHomePosition = -Devices.encoder3.position
        setHumanArmControl()
        setHumanIntakeControl()
        setHumanClawRotationControl()
        setHumanClawReleaseControl()
    }

    val isComplete: Boolean
        get() = !isArmLiftingInProgress()

    /**
     * This method contains five key positions for the arm. Home represents the initial position, with the arm stowed.
     * One through five represent five different positions for the arm to be in.
     * The ONE position is also the position used as the threshold for the auto claw rotation.
     */
    enum class KeyPositions(val position: Int) {
        HOME(0),
        ONE(500),  // temp
        TWO(1000),  // temp
        THREE(1500),  // temp
        FOUR(1750),  // temp
        FIVE(2000) // temp
    }

    enum class ControlMode {
        HUMAN,
        AUTONOMOUS
    }

    /**
     * this method counts how many times Dpad Down has been pressed so that we can use it to turn the
     * intake on and off
     */
    private fun intakeCount(): Int {
        if (Devices.controller2.dpadDown && !dpadPressed) {
            counter += 1.0
            dpadPressed = true
        } else if (!Devices.controller2.dpadDown) dpadPressed = false
        return if (counter % 2 != 0.0) {
            100
        } else {
            0
        }
    }

    /**
     * this method counts how many times Dpad Left has been pressed so that we can use it to open
     * and close the left servo (left grabber)
     */
    private fun leftButtonPressedCount(): Int {
        if (Devices.controller2.dpadRight && !dpadPressedLeft) {
            counterLeft += 1.0
            dpadPressedLeft = true
        } else if (!Devices.controller2.dpadRight) {
            dpadPressedLeft = false
        }
        return if (counterLeft % 2 != 0.0) {
            L_CLOSED
        } else {
            L_OPEN
        }
    }

    fun openLeftGrabber() {
        Devices.servo2.position = R_OPEN.toDouble()
    }

    fun closeLeftGrabber() {
        Devices.servo2.position = R_CLOSED.toDouble()
    }

    /**
     * this method counts how many times Dpad Right has been pressed so that we can use it to open
     * and close the right servo (right grabber)
     */
    private fun rightButtonPressedCount(): Int {
        if (Devices.controller2.dpadLeft && !dpadPressedRight) {
            counterRight += 1.0
            dpadPressedRight = true
        } else if (!Devices.controller2.dpadLeft) {
            dpadPressedRight = false
        }
        return if (counterRight % 2 != 0.0) {
            R_CLOSED
        } else {
            R_OPEN
        }
    }

    fun openRightGrabber() {
        Devices.servo3.position = L_OPEN.toDouble()
    }

    fun closeRightGrabber() {
        Devices.servo3.position = L_CLOSED.toDouble()
    }

    fun autoStartIntake() {
        blockHumanIntakeControl()
        Devices.motor2.power = (-100).toDouble()
    }

    fun autoStopIntake() {
        blockHumanIntakeControl()
        Devices.motor2.power = 0.0
    }

    fun setHumanIntakeControl() {
        autonomousIntakeControl = false
        Devices.motor2.power = (-intakeCount()).toDouble()
    }

    fun blockHumanIntakeControl() {
        autonomousIntakeControl = true
    }

    fun setHumanClawRotationControl() {
        autonomousClawRotationControl = false
    }

    fun blockHumanClawRotationControl() {
        autonomousClawRotationControl = true
    }

    fun autoRotateClaw1(i: Double) {
        blockHumanClawRotationControl()
        Devices.servo0.position = i
    }

    fun autoRotateClaw2(i: Double) {
        blockHumanClawRotationControl()
        Devices.servo1.position = i
    }

    fun autoRaiseArm(pos: KeyPositions) {
        autoRaiseArm(pos.position)
    }

    fun autoRaiseArm(height: Int) {
        blockHumanArmControl()
        armTargetHeight = height
        armLiftingInProgress = true
    }

    fun setHumanArmControl() {
        autonomousArmControl = false
    }

    fun blockHumanArmControl() {
        autonomousArmControl = true
    }

    fun setHumanClawReleaseControl() {
        autonomousClawReleaseControl = false
    }

    fun blockHumanClawReleaseControl() {
        autonomousClawReleaseControl = true
    }

    fun armEmergencyStop() {
        Devices.motor0.power = 0.0
        Devices.motor1.power = 0.0
    }

    val armControlMode: ControlMode
        get() = if (autonomousArmControl) {
            ControlMode.AUTONOMOUS
        } else {
            ControlMode.HUMAN
        }

    val intakeControlMode: ControlMode
        get() = if (autonomousIntakeControl) {
            ControlMode.AUTONOMOUS
        } else {
            ControlMode.HUMAN
        }

    val clawRotationControlMode: ControlMode
        get() = if (autonomousClawRotationControl) {
            ControlMode.AUTONOMOUS
        } else {
            ControlMode.HUMAN
        }

    val clawReleaseControlMode: ControlMode
        get() = if (autonomousClawReleaseControl) {
            ControlMode.AUTONOMOUS
        } else {
            ControlMode.HUMAN
        }

    private val armEncoder: Double
        get() = -Devices.encoder3.position.toDouble()

    private var pixelSelectorServo: Double
        get() = Devices.servo5.position
        set(value) {
            Devices.servo5.position = value
        }

    var pixelPositionSelector: SelectorPositions
        get() {
            if (pixelSelectorServo > 50) return SelectorPositions.LEFT
            return SelectorPositions.RIGHT
        }
        set(value) {
            pixelSelectorServo =    if (value == SelectorPositions.LEFT)    100.0
                                    else                                    0.0
        }

    enum class SelectorPositions {
        LEFT,
        RIGHT
    }

    private var pixelHolderServo: Double
        get() = Devices.servo4.position
        set(value) {
            Devices.servo4.position = value
        }

    var pixelHolderPosition: HolderPositions
        get() {
            if (pixelHolderServo > 50) return HolderPositions.DOWN
            return HolderPositions.UP
        }
        set(value) {
            pixelHolderServo =  if (value == HolderPositions.DOWN)  65.0
                                else                                40.0
        }

    enum class HolderPositions {
        UP,
        DOWN
    }

    fun isArmLiftingInProgress(): Boolean {
        val deltaPos = (armTargetHeight + Devices.encoder3.position).toDouble()
        val motorPower: Int
        if (deltaPos < -50) motorPower = 50
        else if (deltaPos > 50) motorPower = -50
        else {
            armLiftingInProgress = false
            motorPower = 0
            setHumanArmControl()
        }
        Devices.motor0.power = motorPower.toDouble()
        Devices.motor1.power = motorPower.toDouble()
        return autonomousArmControl && armLiftingInProgress
    }

    val armDistanceSensor: Double
        get() = Devices.distanceSensor.distance

    fun servoPickupPos() {
        Devices.servo0.position = 20.0
        Devices.servo1.position = 42.5
    }

    override fun loop() {
        if (Devices.controller2.start) {
            setHumanArmControl()
            armEmergencyStop()
            setHumanIntakeControl()
            setHumanClawRotationControl()
            setHumanClawReleaseControl()
        }

        if (!auto) {
            Logging.log("Servo Holder Positiom", pixelHolderPosition)
            Logging.log("Servo Selector Position", pixelPositionSelector)
            Logging.log("\n-----------------------------\n")
            Logging.log("Arm Control Mode", armControlMode)
            Logging.log("Intake Control Mode", intakeControlMode)
            Logging.log("Claw Rotation Control Mode", clawRotationControlMode)
            Logging.log("Claw Release Control Mode", clawReleaseControlMode)
            Logging.log("Arm Encoder Position", -Devices.encoder3.position)
            Logging.log("Servo0 pos", Devices.servo0.position)
            Logging.log("Servo1 pos", Devices.servo1.position)
            Logging.update()
        }

        val motorPower: Double
        if (!autonomousArmControl) {
            motorPower = -(Devices.controller2.rightTrigger - Devices.controller2.leftTrigger) * 0.75

            if (Devices.controller1.back) {
                encoderHomePosition = -Devices.encoder3.position
            }
        } else {
            val deltaPos = (armTargetHeight + Devices.encoder3.position).toDouble()
            if (deltaPos < -50) motorPower = 75.0
            else if (deltaPos > 50) motorPower = -75.0
            else {
                armLiftingInProgress = false
                motorPower = 0.0
                setHumanArmControl()
            }
        }

        Devices.motor0.power = motorPower
        Devices.motor1.power = motorPower

        if (!autonomousClawReleaseControl) {
            //the motors must move in opposite directions
            Devices.servo3.position = leftButtonPressedCount().toDouble()
            Devices.servo2.position = rightButtonPressedCount().toDouble()
        }

        if (!autonomousClawRotationControl) {
            if (Devices.controller2.triangle) {
                servoPickupPos()
            } else if (Devices.controller2.leftStickButton) {
                Devices.servo0.position = 20.0
                Devices.servo1.position = 13.1
            } else if (Devices.controller2.rightStickButton) {
                Devices.servo0.position = 3.5
                Devices.servo1.position = 18.5
            } else {
                var grabber0Pos = Devices.servo0.position
                grabber0Pos += Devices.controller2.leftStickX * 0.00075
                grabber0Pos = max(0.0, min(100.0, grabber0Pos))
                Devices.servo0.position = grabber0Pos

                var grabber1Pos = Devices.servo1.position
                grabber1Pos += Devices.controller2.rightStickX * 0.00075
                grabber1Pos = max(0.0, min(100.0, grabber1Pos))
                Devices.servo1.position = grabber1Pos
            }
        }

        if (!autonomousIntakeControl) {
            Devices.motor2.power = (-intakeCount()).toDouble()
            if (Devices.controller2.dpadUp) Devices.motor2.power = 100.0
            if (Devices.controller2.leftBumper) {
                pixelPositionSelector = SelectorPositions.LEFT
            } else if (Devices.controller2.rightBumper) {
                pixelPositionSelector = SelectorPositions.RIGHT
            }
            if (Devices.controller2.square) pixelHolderPosition = HolderPositions.DOWN
            else if (Devices.controller2.circle) pixelHolderPosition = HolderPositions.UP
            if (Devices.controller1.rightStickButton) {
                Devices.servo8.position = 100.0
                opMode!!.waitFor(0.5)
                Devices.servo8.position = 50.0
            }
        }
    }

    companion object {
        const val R_OPEN: Int = 10
        const val L_OPEN: Int = 75
        const val R_CLOSED: Int = 0
        const val L_CLOSED: Int = 60
    }
}
