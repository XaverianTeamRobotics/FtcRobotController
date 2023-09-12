package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class LaserDistanceSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val sensor: DistanceSensor

    init {
        sensor = hardwareMap.get(DistanceSensor::class.java, name)
        sensor.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return sensor.getDistance(o as DistanceUnit)
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return DistanceUnit::class.java
    }
}