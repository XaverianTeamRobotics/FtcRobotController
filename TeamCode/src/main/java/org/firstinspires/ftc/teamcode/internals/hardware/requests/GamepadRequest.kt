package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput

class GamepadRequest(private val gamepad: Gamepad, name: String): ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Double {
        when (p0 as GamepadRequestInput) {
            GamepadRequestInput.LEFT_STICK_X ->         return gamepad.left_stick_x.toDouble()
            GamepadRequestInput.LEFT_STICK_Y ->         return gamepad.left_stick_y.toDouble()
            GamepadRequestInput.RIGHT_STICK_X ->        return gamepad.right_stick_x.toDouble()
            GamepadRequestInput.RIGHT_STICK_Y ->        return gamepad.right_stick_y.toDouble()

            GamepadRequestInput.DPAD_UP ->              return gamepad.dpad_up.toDouble()
            GamepadRequestInput.DPAD_DOWN ->            return gamepad.dpad_down.toDouble()
            GamepadRequestInput.DPAD_LEFT ->            return gamepad.dpad_left.toDouble()
            GamepadRequestInput.DPAD_RIGHT ->           return gamepad.dpad_right.toDouble()

            GamepadRequestInput.A ->                    return gamepad.a.toDouble()
            GamepadRequestInput.B ->                    return gamepad.b.toDouble()
            GamepadRequestInput.X ->                    return gamepad.x.toDouble()
            GamepadRequestInput.Y ->                    return gamepad.y.toDouble()

            GamepadRequestInput.LEFT_BUMPER ->          return gamepad.left_bumper.toDouble()
            GamepadRequestInput.RIGHT_BUMPER ->         return gamepad.right_bumper.toDouble()

            GamepadRequestInput.LEFT_TRIGGER ->         return gamepad.left_trigger.toDouble()
            GamepadRequestInput.RIGHT_TRIGGER ->        return gamepad.right_trigger.toDouble()

            GamepadRequestInput.BACK ->                 return gamepad.back.toDouble()
            GamepadRequestInput.START ->                return gamepad.start.toDouble()

            GamepadRequestInput.LEFT_STICK_BUTTON ->    return gamepad.left_stick_button.toDouble()
            GamepadRequestInput.RIGHT_STICK_BUTTON ->   return gamepad.right_stick_button.toDouble()

            GamepadRequestInput.GUIDE ->                return gamepad.guide.toDouble()

            GamepadRequestInput.CIRCLE ->               return gamepad.circle.toDouble()
            GamepadRequestInput.CROSS ->                return gamepad.cross.toDouble()
            GamepadRequestInput.SQUARE ->               return gamepad.square.toDouble()
            GamepadRequestInput.TRIANGLE ->             return gamepad.triangle.toDouble()
            GamepadRequestInput.SHARE ->                return gamepad.share.toDouble()
            GamepadRequestInput.OPTIONS ->              return gamepad.options.toDouble()

            GamepadRequestInput.TOUCHPAD ->             return gamepad.touchpad.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_1 ->    return gamepad.touchpad_finger_1.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_2 ->    return gamepad.touchpad_finger_2.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_1_X ->  return gamepad.touchpad_finger_1_x.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_1_Y ->  return gamepad.touchpad_finger_1_y.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_2_X ->  return gamepad.touchpad_finger_2_x.toDouble()
            GamepadRequestInput.TOUCHPAD_FINGER_2_Y ->  return gamepad.touchpad_finger_2_y.toDouble()

            GamepadRequestInput.PS ->                   return gamepad.ps.toDouble()
        }
    }

    override fun getOutputType(): Class<*> {
        return Double::class.java
    }

    override fun getInputType(): Class<*> {
        return GamepadRequestInput::class.java
    }

    private fun Boolean.toDouble(): Double  = if (this) 1.0 else 0.0
}