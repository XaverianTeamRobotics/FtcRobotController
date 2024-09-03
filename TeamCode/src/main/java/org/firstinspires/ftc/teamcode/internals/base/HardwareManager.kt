package org.firstinspires.ftc.teamcode.internals.base

import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping
import org.firstinspires.ftc.robotcore.external.Telemetry

object HardwareManager {
    lateinit var gamepad1: Gamepad
        private set
    lateinit var gamepad2: Gamepad
        private set

    lateinit var telemetry: Telemetry
        private set

    lateinit var motors: HardwareArray<DcMotor> private set
    lateinit var servos: HardwareArray<Servo> private set
    lateinit var distanceSensor: HardwareArray<DistanceSensor> private set

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var hardwareMap: HardwareMap
    private var initialized = false

    fun init(hardwareMap: HardwareMap, gamepad1: Gamepad, gamepad2: Gamepad, telemetry: Telemetry, secret: String) {
        if (initialized) {
            throw IllegalStateException("HardwareManager has already been initialized")
        }
        if (secret != HardwareSecret.secret) {
            throw HardwareSecret.SecretException()
        }
        HardwareManager.hardwareMap = hardwareMap
        initialized = true
        HardwareManager.gamepad1 = gamepad1
        HardwareManager.gamepad2 = gamepad2
        HardwareManager.telemetry = telemetry

        motors = HardwareArray(hardwareMap.dcMotor, "motor")
        servos = HardwareArray(hardwareMap.servo, "servo")
        val mapping = hardwareMap.DeviceMapping(DistanceSensor::class.java)
        for (sensor in hardwareMap.getAll(DistanceSensor::class.java)) {
            mapping.put(sensor.deviceName, sensor)
        }
        distanceSensor = HardwareArray(mapping, "distanceSensor")
    }

    class HardwareArray <T : HardwareDevice?> internal constructor(val mapping: DeviceMapping<T>, val namePrefix: String) {
        operator fun get(number: Int): T {
            for (set in mapping.entrySet()) {
                if (set.key.startsWith("$namePrefix$number")) {
                    return set.value
                }
            }
            throw IllegalArgumentException("No $namePrefix found with number $number")
        }

        operator fun get(name: String): T {
            for (set in mapping.entrySet()) {
                if (set.key.endsWith(name)) {
                    return set.value
                }
            }
            throw IllegalArgumentException("No $namePrefix found with name $name")
        }

        fun tryGetNamed(name: String, fallback: Int): T {
            return try {
                get(name)
            } catch (e: IllegalArgumentException) {
                get(fallback)
            }
        }
    }
}