package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.data.AnalogData

class AnalogDeviceRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val controller: AnalogInput

    init {
        controller = hardwareMap.get(AnalogInput::class.java, name)
        controller.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return AnalogData(controller.voltage, controller.maxVoltage)
    }

    override fun getOutputType(): Class<*> {
        return AnalogData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}