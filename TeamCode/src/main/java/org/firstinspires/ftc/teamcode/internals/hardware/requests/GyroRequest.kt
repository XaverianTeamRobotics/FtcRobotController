package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.GyroSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.data.GyroData

class GyroRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val gyro: GyroSensor

    init {
        gyro = hardwareMap.get(GyroSensor::class.java, name)
        gyro.resetDeviceConfigurationForOpMode()
        try {
            gyro.calibrate()
            while (gyro.isCalibrating);
        } catch (ignored: UnsupportedOperationException) {
        }
    }

    override fun issueRequest(o: Any): Any {
        return GyroData(
            gyro.heading, intArrayOf(gyro.rawX(), gyro.rawY(), gyro.rawZ()),
            gyro.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return GyroData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}