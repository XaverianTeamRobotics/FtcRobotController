package org.firstinspires.ftc.teamcode.internals

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

     lateinit var motors: HardwareArray<DcMotor>                      private set
     lateinit var servos: HardwareArray<Servo>                        private set
     lateinit var distanceSensor: HardwareArray<DistanceSensor>       private set

     /////////////////////////////////////////////////////////////////////////////////////////////////////////////

     private lateinit var hardwareMap: HardwareMap
     private var initialized = false

     fun init(hardwareMap: HardwareMap, gamepad1: Gamepad, gamepad2: Gamepad, telemetry: Telemetry, secret: String) {
          if (secret != HardwareSecret.secret) {
               throw HardwareSecret.SecretException()
          }
          this.hardwareMap = hardwareMap
          initialized = true
          this.gamepad1 = gamepad1
          this.gamepad2 = gamepad2
          this.telemetry = telemetry

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
               if (!mapping.contains(namePrefix + number)) {
                    throw IllegalArgumentException("No $namePrefix found with number $number")
               }
               if (number < 0) {
                    throw IllegalArgumentException("Number must be positive")
               }

               return returnIfInitialized { mapping[namePrefix + number] }
          }
     }

     private fun <T> returnIfInitialized(block: () -> T): T {
          if (!initialized) {
               throw IllegalStateException("HardwareManager has not been initialized")
          }
          return block()
     }
}