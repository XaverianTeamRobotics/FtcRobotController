package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinOptions

class BlinkinRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val blinkin: RevBlinkinLedDriver
    private var position: Int = 0

    init {
        blinkin = hardwareMap.get(RevBlinkinLedDriver::class.java, name)
        blinkin.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        val (id, type) = o as BlinkinInput
        return if (type === BlinkinOptions.GET) {
            RevBlinkinLedDriver.BlinkinPattern.fromNumber(position)
        } else {
            blinkin.setPattern(id)
            RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE // rgb go zoom zoom!! gaming pc vibes. super fast. 37 ghz
        }
    }

    override fun getOutputType(): Class<*>? {
        return RevBlinkinLedDriver.BlinkinPattern::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return BlinkinInput::class.java
    }
}