package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import android.util.Log
import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput
import org.firstinspires.ftc.teamcode.internals.misc.RobotRebootException
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import java.lang.RuntimeException
import kotlin.jvm.Throws

/**
 * A gamepad is simply a controller that a human uses to control the robot.
 */
class Gamepad(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.jloopingRunner!!.scriptParametersGlobal.getRequest(name) as ScriptParameters.Request

    private var buttonsReg: String = ""
    private var buttonReg: ArrayList<ButtonRegister> = ArrayList()

    private class ButtonRegister {
        var button : GamepadRequestInput;
        var use : String = "";
        constructor(btn: GamepadRequestInput, use : String) {
            button = btn;
            this.use = use;
        }
    }

    private fun registerButton(input: ButtonRegister) {
        checkCollision(input.button)
        buttonsReg += input.button.name + ";"
    }


    /**
     * @param input The button to register.
     * @param use What you intend the button to be used for (used in debugging).
     * Adds the button input to the list of buttons, and registers it as use.
     * If input is already registered, it will throw an error.
     */
    fun registerButton(input: GamepadRequestInput, use: String) {
        registerButton(ButtonRegister(input, use))
    }

    /**
     * Searches through all of the registered buttons and returns
     * the button that matches the string.
     * @param act The useage to search for.
     * @return The button that matches the useage.
     */
    fun buttonSearch(act: String): GamepadRequestInput {

        Logging.log("Button", "Searching for: $act inn array of length ${buttonReg.size}")
        for (i in buttonReg) {
            Logging.log("Button", i.use)
            if (i.use.uppercase() == act.uppercase()) {
                return i.button
            }
        }
        Logging.update()
        org.firstinspires.ftc.teamcode.internals.time.Clock.sleep(3000);
        OperationMode.emergencyStop("Button: $act not registered!")
        return GamepadRequestInput.A // will never be called, is only to satisfy compiler
    }

    fun checkCollision(input: GamepadRequestInput) {
        for (j in buttonReg) {
            if (j.button == input) {
                var msg: String = "Button: " + input.name + " already registered!"
                Logging.log("Contains", input.name)
                if (buttonReg.size > 0) {
                    for (i in buttonReg) {
                        if (i.button == input) {
                            msg += " Registered as: " + i.use
                            Logging.log("Registered as", i.use)
                        }
                    }
                }
                Logging.update()
                OperationMode.emergencyStop(msg)
            }
        }
    }

    val leftStickX: Double
        get() {
            checkCollision(GamepadRequestInput.LEFT_STICK_X)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_X) * 100
        }

    val leftStickY: Double
        get() {
            checkCollision(GamepadRequestInput.LEFT_STICK_Y)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_Y) * 100
        }

    val rightStickX: Double
        get() {
            checkCollision(GamepadRequestInput.RIGHT_STICK_X)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_X) * 100
        }

    val rightStickY: Double
        get() {
            checkCollision(GamepadRequestInput.RIGHT_STICK_Y)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_Y) * 100
        }

    val leftTrigger: Double
        get() {
            checkCollision(GamepadRequestInput.LEFT_TRIGGER)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_TRIGGER) * 100
        }

    val rightTrigger: Double
        get() {
            checkCollision(GamepadRequestInput.RIGHT_TRIGGER)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_TRIGGER) * 100
        }

    val dpadUp: Boolean
        get() {
            checkCollision(GamepadRequestInput.DPAD_UP)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_UP) > 0.5
        }

    val dpadDown: Boolean
        get() {
            checkCollision(GamepadRequestInput.DPAD_DOWN)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_DOWN) > 0.5
        }

    val dpadLeft: Boolean
        get() {
            checkCollision(GamepadRequestInput.DPAD_LEFT)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_LEFT) > 0.5
        }

    val dpadRight: Boolean
        get() {
            checkCollision(GamepadRequestInput.DPAD_RIGHT)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_RIGHT) > 0.5
        }

    val a: Boolean
        get() {
            checkCollision(GamepadRequestInput.A)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.A) > 0.5
        }

    val b: Boolean
        get() {
            checkCollision(GamepadRequestInput.B)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.B) > 0.5
        }

    val x: Boolean
        get() {
            checkCollision(GamepadRequestInput.X)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.X) > 0.5
        }

    val y: Boolean
        get() {
            checkCollision(GamepadRequestInput.Y)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.Y) > 0.5
        }

    val leftBumper: Boolean
        get() {
            checkCollision(GamepadRequestInput.LEFT_BUMPER)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_BUMPER) > 0.5
        }

    val rightBumper: Boolean
        get() {
            checkCollision(GamepadRequestInput.RIGHT_BUMPER)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_BUMPER) > 0.5
        }

    val leftStickButton: Boolean
        get() {
            checkCollision(GamepadRequestInput.LEFT_STICK_BUTTON)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_BUTTON) > 0.5
        }

    val rightStickButton: Boolean
        get() {
            checkCollision(GamepadRequestInput.RIGHT_STICK_BUTTON)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_BUTTON) > 0.5
        }

    val back: Boolean
        get() {
            checkCollision(GamepadRequestInput.BACK)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.BACK) > 0.5
        }

    val start: Boolean
        get() {
            checkCollision(GamepadRequestInput.START)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.START) > 0.5
        }

    val guide: Boolean
        get() {
            checkCollision(GamepadRequestInput.GUIDE)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.GUIDE) > 0.5
        }

    val circle: Boolean
        get() {
            checkCollision(GamepadRequestInput.CIRCLE)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CIRCLE) > 0.5
        }

    val triangle: Boolean
        get() {
            checkCollision(GamepadRequestInput.TRIANGLE)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TRIANGLE) > 0.5
        }

    val square: Boolean
        get() {
            checkCollision(GamepadRequestInput.SQUARE)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SQUARE) > 0.5
        }

    val cross: Boolean
        get() {
            checkCollision(GamepadRequestInput.CROSS)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CROSS) > 0.5
        }

    val share: Boolean
        get() {
            checkCollision(GamepadRequestInput.SHARE)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SHARE) > 0.5
        }

    val options: Boolean
        get() {
            checkCollision(GamepadRequestInput.OPTIONS)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.OPTIONS) > 0.5
        }

    val touchpad: Boolean
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD) > 0.5
        }

    val touchpadFinger1: Boolean
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_1)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1) > 0.5
        }

    val touchpadFinger2: Boolean
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_2)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2) > 0.5
        }

    val touchpadFinger1X: Double
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_1_X)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_X)
        }

    val touchpadFinger1Y: Double
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_1_Y)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_Y)
        }

    val touchpadFinger2X: Double
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_2_X)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_X)
        }

    val touchpadFinger2Y: Double
        get() {
            checkCollision(GamepadRequestInput.TOUCHPAD_FINGER_2_Y)
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_Y)
        }
}
