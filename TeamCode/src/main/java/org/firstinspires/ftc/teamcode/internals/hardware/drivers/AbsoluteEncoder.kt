package org.firstinspires.ftc.teamcode.internals.hardware.drivers

import com.qualcomm.robotcore.hardware.AnalogInputController
import com.qualcomm.robotcore.hardware.AnalogSensor
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties

@DeviceProperties(
    name = "Absolute Encoder",
    xmlTag = "absoluteEncoder"
)
@AnalogSensorType
class AbsoluteEncoder(private val controller: AnalogInputController, private val port: Int): HardwareDevice, AnalogSensor {
    var maxVoltage: Double = 3.3
        set(value) {
            // cap value between minVoltage and 3.3
            field = value.coerceIn(minVoltage, 3.3)
        }

    var minVoltage: Double = 0.0
        set(value) {
            // cap value between 0 and maxVoltage
            field = value.coerceIn(0.0, maxVoltage)
        }

    val position: Double
        get() = readRawVoltage() / (maxVoltage - minVoltage)

    override fun getManufacturer(): Manufacturer {
        return Manufacturer.Other
    }

    override fun getDeviceName(): String {
        return "Absolute Encoder"
    }

    override fun getConnectionInfo(): String {
        return "Absolute Encoder | Port: $port"
    }

    override fun getVersion(): Int {
        return 1
    }

    override fun resetDeviceConfigurationForOpMode() {}

    override fun close() {}

    override fun readRawVoltage(): Double {
        return controller.getAnalogInputVoltage(port)
    }
}