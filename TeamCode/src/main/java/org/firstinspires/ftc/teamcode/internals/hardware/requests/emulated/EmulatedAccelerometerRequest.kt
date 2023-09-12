package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.internals.hardware.data.AccelerometerData

class EmulatedAccelerometerRequest(name: String) : ScriptParameters.Request(name) {

    override fun issueRequest(p0: Any?): Any {
        return AccelerometerData(Acceleration(), "")
    }

    override fun getOutputType(): Class<*> {
        return AccelerometerData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }


}