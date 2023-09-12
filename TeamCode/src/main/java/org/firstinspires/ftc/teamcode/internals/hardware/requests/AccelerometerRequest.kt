package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AccelerationSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.data.AccelerometerData

class AccelerometerRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val accelerometer: AccelerationSensor

    init {
        accelerometer = hardwareMap.get(AccelerationSensor::class.java, name)
        accelerometer.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return AccelerometerData(
            accelerometer.acceleration,
            accelerometer.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return AccelerometerData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}