package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters

class EmulatedVoltageSensorRequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return 0.0
    }

    override fun getOutputType(): Class<Double>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }

}