package org.firstinspires.ftc.teamcode.internals.hardware.data

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor

data class ColorSensors(val sanitized: NormalizedColorSensor, val raw: ColorSensor)