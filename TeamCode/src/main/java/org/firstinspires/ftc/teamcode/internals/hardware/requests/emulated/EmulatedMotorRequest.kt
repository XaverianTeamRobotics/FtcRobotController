package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.StandardMotorParameters

class EmulatedMotorRequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(o: Any?): Any {
        var input = o as StandardMotorParameters
        println("Emulated motor set to ${input.value} with mode ${input.operation}")
        return 0
    }

    override fun getOutputType(): Class<*>? {
        return Int::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return StandardMotorParameters::class.java
    }
}