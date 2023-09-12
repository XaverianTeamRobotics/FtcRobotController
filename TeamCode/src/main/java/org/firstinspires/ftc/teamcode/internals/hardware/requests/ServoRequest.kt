package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.internals.hardware.data.ServoInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.ServoOptions

class ServoRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val servo: Servo

    init {
        servo = hardwareMap.get(Servo::class.java, name)
        servo.resetDeviceConfigurationForOpMode()
        servo.direction = Servo.Direction.FORWARD
    }

    override fun issueRequest(o: Any): Any {
        val vals = o as ServoInput
        return if (vals.type === ServoOptions.GET) {
            servo.position * 100.0
        } else {
            var `val` = vals.position
            `val` = Range.clip(`val`, 0.0, 100.0)
            servo.position = `val` / 100
            0.0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return ServoInput::class.java
    }
}