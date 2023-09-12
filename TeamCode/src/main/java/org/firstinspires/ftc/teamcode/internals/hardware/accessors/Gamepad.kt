package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput

/**
 * A gamepad is simply a controller that a human uses to control the robot.
 */
class Gamepad(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.jloopingRunner!!.scriptParametersGlobal.getRequest(name) as ScriptParameters.Request

    val leftStickX: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_X) * 100
        }

    val leftStickY: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_Y) * 100
        }

    val rightStickX: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_X) * 100
        }

    val rightStickY: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_Y) * 100
        }

    val leftTrigger: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_TRIGGER) * 100
        }

    val rightTrigger: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_TRIGGER) * 100
        }

    val dpadUp: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_UP) > 0.5
        }

    val dpadDown: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_DOWN) > 0.5
        }

    val dpadLeft: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_LEFT) > 0.5
        }

    val dpadRight: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_RIGHT) > 0.5
        }

    val a: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.A) > 0.5
        }

    val b: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.B) > 0.5
        }

    val x: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.X) > 0.5
        }

    val y: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.Y) > 0.5
        }

    val leftBumper: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_BUMPER) > 0.5
        }

    val rightBumper: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_BUMPER) > 0.5
        }

    val leftStickButton: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_BUTTON) > 0.5
        }

    val rightStickButton: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_BUTTON) > 0.5
        }

    val back: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.BACK) > 0.5
        }

    val start: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.START) > 0.5
        }

    val guide: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.GUIDE) > 0.5
        }

    val circle: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CIRCLE) > 0.5
        }

    val triangle: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TRIANGLE) > 0.5
        }

    val square: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SQUARE) > 0.5
        }

    val cross: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CROSS) > 0.5
        }

    val share: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SHARE) > 0.5
        }

    val options: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.OPTIONS) > 0.5
        }

    val touchpad: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD) > 0.5
        }

    val touchpadFinger1: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1) > 0.5
        }

    val touchpadFinger2: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2) > 0.5
        }

    val touchpadFinger1X: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_X)
        }

    val touchpadFinger1Y: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_Y)
        }

    val touchpadFinger2X: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_X)
        }

    val touchpadFinger2Y: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_Y)
        }
}
