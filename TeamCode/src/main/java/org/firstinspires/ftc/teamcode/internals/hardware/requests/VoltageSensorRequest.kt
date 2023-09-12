package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor

class VoltageSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val sensor: VoltageSensor

    init {
        sensor = hardwareMap.get(VoltageSensor::class.java, name)
        sensor.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return sensor.voltage
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}