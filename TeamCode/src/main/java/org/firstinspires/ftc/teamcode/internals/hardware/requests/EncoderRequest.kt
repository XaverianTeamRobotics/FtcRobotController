package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.InitializedDCDevices
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderPositionStore

class EncoderRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val encoder: DcMotor
    private var offset: Int
    private var realOffset: Int

    init {
        var newName = name
        if (name.startsWith("-")) {
            newName = name.substring(1)
        }
        encoder = hardwareMap.get(DcMotor::class.java, newName)
        if (!InitializedDCDevices.has(name)) {
            encoder.resetDeviceConfigurationForOpMode()
            encoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            encoder.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            encoder.power = 0.0
            offset = encoder.currentPosition
            InitializedDCDevices.add(name)
        } else {
            offset = encoder.currentPosition
        }
        realOffset = EncoderPositionStore.getPosition(name) ?: 0
    }

    override fun issueRequest(o: Any): Any {
        val input = o as EncoderInput
        return if (input === EncoderInput.GET) {
            encoder.currentPosition - offset + realOffset
        } else {
            offset = encoder.currentPosition
            realOffset = 0
            0
        }
    }

    override fun getOutputType(): Class<*> {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return EncoderInput::class.java
    }
}