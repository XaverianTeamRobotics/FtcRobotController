package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.internals.hardware.data.ContinousServoInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.ContinousServoOptions

class ContinousServoRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val servo: CRServo

    init {
        servo = hardwareMap.get(CRServo::class.java, name)
        servo.resetDeviceConfigurationForOpMode()
        servo.direction = DcMotorSimple.Direction.FORWARD
    }

    override fun issueRequest(o: Any): Any {
        val input = o as ContinousServoInput
        return if (input.type === ContinousServoOptions.GET) {
            servo.power * 100
        } else {
            var `val` = input.power
            `val` = Range.clip(`val`, -100.0, 100.0)
            servo.power = `val` / 100
            0.0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return ContinousServoInput::class.java
    }
}