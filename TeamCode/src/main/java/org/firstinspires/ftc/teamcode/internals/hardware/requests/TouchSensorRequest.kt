package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.TouchSensor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.internals.hardware.data.TouchSensorData

class TouchSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val sensor: TouchSensor

    init {
        sensor = hardwareMap.get(TouchSensor::class.java, name)
        sensor.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return TouchSensorData(sensor.isPressed, Range.clip(sensor.value * 100, 0.0, 100.0))
    }

    override fun getOutputType(): Class<*> {
        return TouchSensorData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}