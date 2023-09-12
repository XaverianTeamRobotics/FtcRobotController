package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.IMUData
import org.firstinspires.ftc.teamcode.internals.misc.Vector3

class EmulatedIMURequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return IMUData(
            Vector3(0.0, 0.0, 0.0),
            Vector3(0.0, 0.0, 0.0)
        )
    }

    override fun getOutputType(): Class<*> {
        return IMUData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}