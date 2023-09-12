package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.Colors

class EmulatedColorSensorRequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return Colors(
            intArrayOf(0, 0, 0, 0),
            doubleArrayOf(0.0, 0.0, 0.0),
            0,
            0.0,
            0
        )
    }

    override fun getOutputType(): Class<*> {
        return Colors::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}