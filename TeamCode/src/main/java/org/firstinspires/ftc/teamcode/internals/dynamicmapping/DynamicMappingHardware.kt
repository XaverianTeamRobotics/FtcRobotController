package org.firstinspires.ftc.teamcode.internals.dynamicmapping

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import kotlin.reflect.KClass

enum class DynamicMappingHardware private constructor(val type: KClass<*>) {
    MOTOR(DcMotor::class),
    SERVO(Servo::class),
    SENSOR_ENCODER(DcMotor::class),
    SENSOR_DISTANCE(DistanceSensor::class),
}