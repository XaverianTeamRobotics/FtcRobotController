package org.firstinspires.ftc.teamcode.internals.hardware

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.HardwareMap.DeviceMapping
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Manages the hardware components of the robot.
 */
object HardwareManager {
    /**
     * Gamepad 1 used for controlling the robot.
     */
    @JvmStatic
    lateinit var gamepad1: Gamepad
        private set

    /**
     * Gamepad 2 used for controlling the robot.
     */
    @JvmStatic
    lateinit var gamepad2: Gamepad
        private set

    /**
     * Telemetry object for sending data to the driver station.
     */
    @JvmStatic
    lateinit var telemetry: Telemetry
        private set

    /**
     * Array of DC motors.
     */
    @JvmStatic
    lateinit var motors: HardwareArray<DcMotor> private set

    /**
     * Array of servos.
     */
    @JvmStatic
    lateinit var servos: HardwareArray<Servo> private set

    /**
     * Array of distance sensors.
     */
    @JvmStatic
    lateinit var distanceSensor: HardwareArray<DistanceSensor> private set

    /**
     * Array of touch sensors.
     */
    @JvmStatic
    lateinit var touchSwitches: HardwareArray<TouchSensor> private set

    /**
     * Limelight 3A vision sensor.
     */
    @JvmStatic
    var limelight3A: Limelight3A? = null
        private set

    /**
     * Hardware map containing all hardware devices.
     */
    @JvmStatic
    lateinit var hardwareMap: HardwareMap
        private set

    /**
     * Initializes the hardware manager with the provided hardware map, gamepads, telemetry, and secret.
     *
     * @param hardwareMap The hardware map to use.
     * @param gamepad1 The first gamepad.
     * @param gamepad2 The second gamepad.
     * @param telemetry The telemetry object.
     * @param secret The secret key for initialization.
     * @throws HardwareSecret.SecretException if the provided secret is incorrect.
     */
    fun init(hardwareMap: HardwareMap, gamepad1: Gamepad, gamepad2: Gamepad, telemetry: Telemetry, secret: String) {
        if (secret != HardwareSecret.secret) {
            throw HardwareSecret.SecretException()
        }
        HardwareManager.hardwareMap = hardwareMap
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

        touchSwitches = HardwareArray(hardwareMap.touchSensor, "ts")

        try {
            limelight3A = hardwareMap.get(Limelight3A::class.java, "limelight")
        } catch (_: Exception) {}
    }

    /**
     * Wrapper class for managing arrays of hardware devices.
     *
     * @param T The type of hardware device.
     * @property mapping The device mapping for the hardware devices.
     * @property namePrefix The prefix for the device names.
     */
    class HardwareArray <T : HardwareDevice?> internal constructor(val mapping: DeviceMapping<T>, val namePrefix: String) {
        /**
         * Gets a hardware device by its number.
         *
         * @param number The number of the device.
         * @return The hardware device.
         * @throws IllegalArgumentException if no device is found with the given number.
         */
        operator fun get(number: Int): T {
            for (set in mapping.entrySet()) {
                if (set.key.startsWith("$namePrefix$number")) {
                    return set.value
                }
            }
            try {
                return hardwareMap.get("$namePrefix$number") as T
            } catch (_: Exception) {}
            throw IllegalArgumentException("No $namePrefix found with number $number")
        }

        /**
         * Gets a hardware device by its name.
         *
         * @param name The name of the device.
         * @return The hardware device.
         * @throws IllegalArgumentException if no device is found with the given name.
         */
        operator fun get(name: String): T {
            for (set in mapping.entrySet()) {
                if (set.key.endsWith(name)) {
                    return set.value
                }
            }
            throw IllegalArgumentException("No $namePrefix found with name $name")
        }

        /**
         * Gets a hardware device by its name, with a fallback to a device number.
         *
         * @param name The name of the device.
         * @param fallback The fallback number of the device.
         * @return The hardware device.
         */
        fun get(name: String, fallback: Int): T {
            return try {
                get(name)
            } catch (e: IllegalArgumentException) {
                RobotLog.w("No $namePrefix found with $name, trying fallback $fallback")
                get(fallback)
            }
        }
    }
}