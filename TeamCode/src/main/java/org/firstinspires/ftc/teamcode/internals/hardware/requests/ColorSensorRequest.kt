package org.firstinspires.ftc.teamcode.internals.hardware.requests

import android.graphics.Color
import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import org.firstinspires.ftc.teamcode.internals.hardware.data.Colors
import org.firstinspires.ftc.teamcode.internals.hardware.data.ColorSensors

class ColorSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val NORMALIZED_COLORS: NormalizedColorSensor
    private val RAW: ColorSensor

    val sensors: ColorSensors

    init {
        NORMALIZED_COLORS = hardwareMap.get(NormalizedColorSensor::class.java, name)
        RAW = hardwareMap.get(ColorSensor::class.java, name)
        sensors = ColorSensors(NORMALIZED_COLORS, RAW)
    }

    override fun issueRequest(o: Any): Any {
        val vals = IntArray(4)
        vals[0] = (NORMALIZED_COLORS.normalizedColors.red * 255).toInt()
        vals[1] = (NORMALIZED_COLORS.normalizedColors.green * 255).toInt()
        vals[2] = (NORMALIZED_COLORS.normalizedColors.blue * 255).toInt()
        vals[3] = (NORMALIZED_COLORS.normalizedColors.alpha * 255).toInt()
        val colors = FloatArray(3)
        Color.RGBToHSV(vals[0], vals[1], vals[2], colors)
        val colorsd = DoubleArray(3)
        colorsd[0] = colors[0].toDouble()
        colorsd[1] = colors[1].toDouble()
        colorsd[2] = colors[2].toDouble()
        val gray = vals[0] + vals[1] + vals[2]
        return Colors(
            vals,
            colorsd,
            gray,
            NORMALIZED_COLORS.gain.toDouble(),
            RAW.argb()
        )
    }

    override fun getOutputType(): Class<*> {
        return Colors::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}