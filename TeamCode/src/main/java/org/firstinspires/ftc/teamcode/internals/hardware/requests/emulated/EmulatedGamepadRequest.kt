package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput

class EmulatedGamepadRequest(name: String): ScriptParameters.Request(name) {
    init {
        val variable = ScriptParameters.GlobalVariable<GamepadEmulatedValue>(name)
        variable.value = GamepadEmulatedValue()
        HardwareGetter.jloopingRunner!!.scriptParametersGlobal.addGlobalVariable(variable)
    }

    data class GamepadEmulatedValue(   var left_stick_x         : Double = 0.0,
                                       var left_stick_y         : Double = 0.0,
                                       var right_stick_x        : Double = 0.0,
                                       var right_stick_y        : Double = 0.0,
                                       var dpad_up              : Double = 0.0,
                                       var dpad_down            : Double = 0.0,
                                       var dpad_left            : Double = 0.0,
                                       var dpad_right           : Double = 0.0,
                                       var a                    : Double = 0.0,
                                       var b                    : Double = 0.0,
                                       var x                    : Double = 0.0,
                                       var y                    : Double = 0.0,
                                       var left_bumper          : Double = 0.0,
                                       var right_bumper         : Double = 0.0,
                                       var left_trigger         : Double = 0.0,
                                       var right_trigger        : Double = 0.0,
                                       var back                 : Double = 0.0,
                                       var start                : Double = 0.0,
                                       var left_stick_button    : Double = 0.0,
                                       var right_stick_button   : Double = 0.0,
                                       var guide                : Double = 0.0,
                                       var circle               : Double = 0.0,
                                       var cross                : Double = 0.0,
                                       var square               : Double = 0.0,
                                       var triangle             : Double = 0.0,
                                       var share                : Double = 0.0,
                                       var options              : Double = 0.0,
                                       var touchpad             : Double = 0.0,
                                       var touchpad_finger_1    : Double = 0.0,
                                       var touchpad_finger_2    : Double = 0.0,
                                       var touchpad_finger_1_x  : Double = 0.0,
                                       var touchpad_finger_1_y  : Double = 0.0,
                                       var touchpad_finger_2_x  : Double = 0.0,
                                       var touchpad_finger_2_y  : Double = 0.0,
                                       var ps                   : Double = 0.0)

    override fun issueRequest(p0: Any?): Any {
        val gamepad: GamepadEmulatedValue = HardwareGetter.jloopingRunner!!.scriptParametersGlobal.getGlobalVariable("emulatedGamepad$name").value as GamepadEmulatedValue
        when (p0 as GamepadRequestInput) {
            GamepadRequestInput.LEFT_STICK_X ->         return gamepad.left_stick_x
            GamepadRequestInput.LEFT_STICK_Y ->         return gamepad.left_stick_y
            GamepadRequestInput.RIGHT_STICK_X ->        return gamepad.right_stick_x
            GamepadRequestInput.RIGHT_STICK_Y ->        return gamepad.right_stick_y
            GamepadRequestInput.DPAD_UP ->              return gamepad.dpad_up
            GamepadRequestInput.DPAD_DOWN ->            return gamepad.dpad_down
            GamepadRequestInput.DPAD_LEFT ->            return gamepad.dpad_left
            GamepadRequestInput.DPAD_RIGHT ->           return gamepad.dpad_right
            GamepadRequestInput.A ->                    return gamepad.a
            GamepadRequestInput.B ->                    return gamepad.b
            GamepadRequestInput.X ->                    return gamepad.x
            GamepadRequestInput.Y ->                    return gamepad.y
            GamepadRequestInput.LEFT_BUMPER ->          return gamepad.left_bumper
            GamepadRequestInput.RIGHT_BUMPER ->         return gamepad.right_bumper
            GamepadRequestInput.LEFT_TRIGGER ->         return gamepad.left_trigger
            GamepadRequestInput.RIGHT_TRIGGER ->        return gamepad.right_trigger
            GamepadRequestInput.BACK ->                 return gamepad.back
            GamepadRequestInput.START ->                return gamepad.start
            GamepadRequestInput.LEFT_STICK_BUTTON ->    return gamepad.left_stick_button
            GamepadRequestInput.RIGHT_STICK_BUTTON ->   return gamepad.right_stick_button
            GamepadRequestInput.GUIDE ->                return gamepad.guide
            GamepadRequestInput.CIRCLE ->               return gamepad.circle
            GamepadRequestInput.CROSS ->                return gamepad.cross
            GamepadRequestInput.SQUARE ->               return gamepad.square
            GamepadRequestInput.TRIANGLE ->             return gamepad.triangle
            GamepadRequestInput.SHARE ->                return gamepad.share
            GamepadRequestInput.OPTIONS ->              return gamepad.options
            GamepadRequestInput.TOUCHPAD ->             return gamepad.touchpad
            GamepadRequestInput.TOUCHPAD_FINGER_1 ->    return gamepad.touchpad_finger_1
            GamepadRequestInput.TOUCHPAD_FINGER_2 ->    return gamepad.touchpad_finger_2
            GamepadRequestInput.TOUCHPAD_FINGER_1_X ->  return gamepad.touchpad_finger_1_x
            GamepadRequestInput.TOUCHPAD_FINGER_1_Y ->  return gamepad.touchpad_finger_1_y
            GamepadRequestInput.TOUCHPAD_FINGER_2_X ->  return gamepad.touchpad_finger_2_x
            GamepadRequestInput.TOUCHPAD_FINGER_2_Y ->  return gamepad.touchpad_finger_2_y
            GamepadRequestInput.PS ->                   return gamepad.ps
        }
    }

    override fun getOutputType(): Class<*> {
        return Double::class.java
    }

    override fun getInputType(): Class<*> {
        return GamepadRequestInput::class.java
    }
}