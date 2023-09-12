package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.LightSensor
import org.firstinspires.ftc.teamcode.internals.hardware.data.LightSensorData

class LightSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val sensor: LightSensor
    private var led = true

    init {
        sensor = hardwareMap.get(LightSensor::class.java, name)
        sensor.enableLed(true)
    }

    override fun issueRequest(o: Any): Any {
        val change = o as Boolean
        if(change != led) {
            sensor.enableLed(change)
            led = change
        }
        return LightSensorData(
            sensor.rawLightDetected, sensor.rawLightDetectedMax,
            sensor.lightDetected, sensor.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return LightSensorData::class.java
    }

    override fun getInputType(): Class<*>? {
        return Boolean::class.javaPrimitiveType
    }
}