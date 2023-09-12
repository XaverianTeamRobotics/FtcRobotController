package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.LightSensorData

class EmulatedLightSensorRequest(name: String) : ScriptParameters.Request(name) {
    private var enabled = true

    override fun issueRequest(p0: Any?): Any {
        val enable = p0 as Boolean
        if(enable == !enabled) {
            enabled = enable
            println("LED status changed to $enable.")
        }
        return LightSensorData(0.0, 0.0, 0.0, "")
    }

    override fun getOutputType(): Class<*> {
        return LightSensorData::class.java
    }

    override fun getInputType(): Class<Boolean>? {
        return Boolean::class.javaPrimitiveType
    }
}