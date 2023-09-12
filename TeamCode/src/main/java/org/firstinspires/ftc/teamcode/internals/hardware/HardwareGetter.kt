package org.firstinspires.ftc.teamcode.internals.hardware

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptRunner
import com.michaell.looping.ScriptTemplate
import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.LightSensor
import com.qualcomm.robotcore.hardware.TouchSensor
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.hardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.*
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.IMU
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Servo
import org.firstinspires.ftc.teamcode.internals.hardware.data.*
import org.firstinspires.ftc.teamcode.internals.hardware.requests.*
import org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated.*
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import com.qualcomm.robotcore.hardware.Servo as QualcommServo

class HardwareGetter {
    companion object {
        @JvmStatic
        var isEmulated: Boolean = false
            get() = field
            set(value) {
                field = value
            }

        @JvmStatic
        var hardwareMap: HardwareMap? = null
            get() = field
            set(value) {
                if (value == null) {
                    throw IllegalArgumentException("The global hardwareMap cannot be set to null during execution")
                } else {
                    field = value
                }
            }

        @JvmStatic
        var jloopingRunner: ScriptRunner? = null
            get() = field
            set(value) {
                if (value == null) {
                    throw IllegalArgumentException("The global ScriptRunner cannot be set to null during execution")
                } else {
                    field = value
                }
            }

        @JvmStatic
        var opMode: OperationMode? = null
            get() = field
            set(value) {
                if (value == null) {
                    throw IllegalArgumentException("The global OpMode cannot be set to null during execution")
                } else {
                    field = value
                }
            }

        /**
         * Creates a GamepadRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeGamepadRequest(name: String, gamepad: com.qualcomm.robotcore.hardware.Gamepad?): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedGamepadRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = GamepadRequest(gamepad!!, name)
            jloopingRunner!!.addRequest(req)
            return req
        }

        @JvmStatic
        fun getGamepadValue(name: String, gamepadRequestInput: GamepadRequestInput): Double {
            val n: String = if (isEmulated) {
                "emulatedGamepad$name"
            } else {
                name
            }

            return jloopingRunner!!.scriptParametersGlobal.issueRequest(gamepadRequestInput, jloopingRunner!!
                .scriptParametersGlobal.getRequest(n)) as Double
        }

        /**
         * Creates a StandardMotorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeMotorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedMotorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = MotorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get motor from a previously initialized request
         */
        @JvmStatic
        fun getMotorFromRequest(name: String): DcMotor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as MotorRequest).motor
        }

        @JvmStatic
        fun setMotorValue(name: String, value: StandardMotorParameters) {
            jloopingRunner!!.scriptParametersGlobal.issueRequest(value, jloopingRunner!!.scriptParametersGlobal.getRequest(name))
        }

        /**
         * Check if a motor is currently busy. This can be used in autonomous programming to track if a encoder is
         * done going a certain distance.
         */
        @JvmStatic
        fun isMotorBusy(name: String): Boolean {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    return false
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return getMotorFromRequest(name).isBusy
        }

        /**
         * Creates a StandardAccelerometerRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeAccelerometerRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedAccelerometerRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = AccelerometerRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get accelerometer from a previously initialized request
         */
        @JvmStatic
        fun getAccelerometerFromRequest(name: String): AccelerationSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as AccelerometerRequest).accelerometer
        }

        @JvmStatic
        fun getAccelerometerData(name: String): AccelerometerData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!
                .scriptParametersGlobal.getRequest(name)) as AccelerometerData
        }

        /**
         * Creates a StandardAnalogDeviceRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeAnalogDeviceRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedAnalogDeviceRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = AnalogDeviceRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get AnalogDevice from a previously initialized request
         */
        @JvmStatic
        fun getAnalogDeviceFromRequest(name: String): AnalogInput {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as AnalogDeviceRequest).controller
        }

        @JvmStatic
        fun getAnalogDeviceData(name: String): AnalogData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!
                .scriptParametersGlobal.getRequest(name)) as AnalogData
        }

        /**
         * Creates a StandardBlinkinRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeBlinkinRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedBlinkinRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = BlinkinRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get Blinkin from a previously initialized request
         */
        @JvmStatic
        fun getBlinkinFromRequest(name: String): RevBlinkinLedDriver {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as BlinkinRequest).blinkin
        }

        @JvmStatic
        fun issueBlinkinRequest(name: String, input: BlinkinInput): RevBlinkinLedDriver.BlinkinPattern {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(input, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as RevBlinkinLedDriver.BlinkinPattern
        }

        /**
         * Creates a StandardColorSensorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeColorSensorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedColorSensorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = ColorSensorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get ColorSensor from a previously initialized request
         */
        @JvmStatic
        fun getColorSensorFromRequest(name: String): ColorSensors {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as ColorSensorRequest).sensors
        }

        @JvmStatic
        fun getColorSensorData(name: String): Colors {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Colors
        }

        /**
         * Creates a StandardContinousServoRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeContinousServoRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedContinousServoRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = ContinousServoRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get ContinousServo from a previously initialized request
         */
        @JvmStatic
        fun getContinousServoFromRequest(name: String): CRServo {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as ContinousServoRequest).servo
        }

        @JvmStatic
        fun issueContinousServoRequest(name: String, input: ContinousServoInput): Double {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(input, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Double
        }

        /**
         * Creates a StandardEncoderRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeEncoderRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedEncoderRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = EncoderRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get Encoder from a previously initialized request
         */
        @JvmStatic
        fun getEncoderFromRequest(name: String): DcMotor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as EncoderRequest).encoder
        }

        @JvmStatic
        fun issueEncoderRequest(name: String, input: EncoderInput): Int {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(input, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Int
        }

        /**
         * Creates a StandardGyroscopeRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeGyroscopeRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedGyroRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = GyroRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get Gyroscope from a previously initialized request
         */
        @JvmStatic
        fun getGyroscopeFromRequest(name: String): GyroSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as GyroRequest).gyro
        }

        @JvmStatic
        fun getGyroData(name: String): GyroData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as GyroData
        }

        /**
         * Creates a StandardLaserDistanceSensorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeLaserDistanceSensorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedLaserDistanceSensorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = LaserDistanceSensorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get LaserDistanceSensor from a previously initialized request
         */
        @JvmStatic
        fun getLaserDistanceSensorFromRequest(name: String): DistanceSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as LaserDistanceSensorRequest).sensor
        }

        @JvmStatic
        fun issueLaserDistanceSensorRequest(name: String, unit: DistanceUnit): Double {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(unit, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Double
        }

        /**
         * Creates a StandardLightSensorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeLightSensorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedLightSensorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = LightSensorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get LightSensor from a previously initialized request
         */
        @JvmStatic
        fun getLightSensorFromRequest(name: String): LightSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as LightSensorRequest).sensor
        }

        @JvmStatic
        fun issueLightSensorRequest(name: String, change: Boolean): LightSensorData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(change, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as LightSensorData
        }

        /**
         * Creates a StandardServoRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeServoRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedServoRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = ServoRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get Servo from a previously initialized request
         */
        @JvmStatic
        fun getServoFromRequest(name: String): QualcommServo {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as ServoRequest).servo
        }

        @JvmStatic
        fun issueServoRequest(name: String, input: ServoInput): Double {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(input, jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Double
        }

        /**
         * Creates a StandardTouchSensorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeTouchSensorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedTouchSensorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = TouchSensorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get TouchSensor from a previously initialized request
         */
        @JvmStatic
        fun getTouchSensorFromRequest(name: String): TouchSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as TouchSensorRequest).sensor
        }

        @JvmStatic
        fun getTouchSensorData(name: String): TouchSensorData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as TouchSensorData
        }

        /**
         * Creates a StandardVoltageSensorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeVoltageSensorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedVoltageSensorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = VoltageSensorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get VoltageSensor from a previously initialized request
         */
        @JvmStatic
        fun getVoltageSensorFromRequest(name: String): VoltageSensor {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    throw NullPointerException("This is running in the emulator!")
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as VoltageSensorRequest).sensor
        }

        @JvmStatic
        fun getVoltageSensorData(name: String): Double {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as Double
        }

        /**
         * Create a IMURequest, add it to the global JloopingRunner, then return it
         */
        @JvmStatic
        fun makeIMURequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedIMURequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = IMURequest(name)
            jloopingRunner!!.addRequest(req)
            return req
        }

        @JvmStatic
        fun getIMUData(name: String): IMUData {
            return jloopingRunner!!.scriptParametersGlobal.issueRequest(Any(), jloopingRunner!!.scriptParametersGlobal.getRequest(name)) as IMUData
        }

        /**
         * Inits standard devices which can be assumed to exist on all robots. Other devices must be initialized inside <code>OperationMode.construct()</code>.
         */
        @JvmStatic
        fun initStdDevices() {
            Devices.controller1 = Gamepad("gamepad1")
            Devices.controller2 = Gamepad("gamepad2")
        }

    }
}

/**
 * This keeps track of a list of all motors and encoders which were initialized. Required to initialize them properly.
 */
object InitializedDCDevices {

    private val devices = HashMap<String, Boolean>()

    @JvmStatic
    fun has(name: String): Boolean {
        return devices.containsKey(name)
    }

    @JvmStatic
    fun add(name: String) {
        devices[name] = true
    }

}

class Devices {
    companion object {

        /*

        ADDING HARDWARE DEVICES:
        1. Create a lateinit variable here with the name of the hardware device and annotate it with @JvmStatic
        2. Set the type to either a subtype of DeviceAccessor or a subtype of HardwareDevice
        3. Go to /src/main/res/xml/main_config.xml and follow the steps there

         */

        @JvmStatic lateinit var controller1: Gamepad
        @JvmStatic lateinit var controller2: Gamepad
        @JvmStatic lateinit var motor0: Motor
        @JvmStatic lateinit var motor1: Motor
        @JvmStatic lateinit var motor2: Motor
        @JvmStatic lateinit var motor3: Motor
        @JvmStatic lateinit var motor4: Motor
        @JvmStatic lateinit var motor5: Motor
        @JvmStatic lateinit var motor6: Motor
        @JvmStatic lateinit var motor7: Motor
        @JvmStatic lateinit var servo0: Servo
        @JvmStatic lateinit var servo1: Servo
        @JvmStatic lateinit var servo2: Servo
        @JvmStatic lateinit var camera0: WebcamName
        @JvmStatic lateinit var camera1: WebcamName
        @JvmStatic lateinit var imu: IMU
        @JvmStatic lateinit var distanceSensor: LaserDistanceSensor
        @JvmStatic lateinit var armDistanceSensor: LaserDistanceSensor
        @JvmStatic lateinit var encoder5: Encoder
        @JvmStatic lateinit var encoder6: Encoder
    }
}

class GamepadBinding(val button: GamepadRequestInput, val gamepad: Gamepad, val action: (Double) -> Unit) : ScriptTemplate("GamepadBinding${gamepad.name}$button", false) {
    override fun run(p0: ScriptParameters?) {
        action(gamepad.request.issueRequest(button) as Double)
    }
}

/**
 * Initializes devices from Devices.Companion which are found in the current RC configuration. Should be executed by jlooping prior to OperationMode.construct().
 */
fun initConfigDevices() {
    // find our devices
    val devices = Devices::class.java.declaredFields
    val map = hardwareMap?.getAllNames(HardwareDevice::class.java)
    for(mappedDevice in map!!) {
        val deviceArr = arrayOf(DeviceToMake(null, null), DeviceToMake(null, null))
        // handle dual encoder/motor instances--we delimit the names with "-" because we can't use that in fields
        if(mappedDevice.contains("-")) {
            val deviceM = devices.find { device ->
                val nameBeginning = device.name.lastIndexOf(".") + 1
                val name = device.name.substring(nameBeginning)
                return@find name == mappedDevice.split("-")[0]
            }
            val deviceE = devices.find { device ->
                val nameBeginning = device.name.lastIndexOf(".") + 1
                val name = device.name.substring(nameBeginning)
                return@find name == mappedDevice.split("-")[1]
            }
            deviceArr[0].device = deviceM
            deviceArr[0].name = mappedDevice
            deviceArr[1].device = deviceE
            deviceArr[1].name = "-$mappedDevice"
        }else{
            // for each device in the config, find its corresponding device in Devices.Companion
            val deviceN = devices.find { device ->
                val nameBeginning = device.name.lastIndexOf(".") + 1
                val name = device.name.substring(nameBeginning)
                return@find name == mappedDevice
            }
            deviceArr[0].device = deviceN
            deviceArr[0].name = mappedDevice
        }
        // for each device:
        // if the device is a DeviceAccessor, lets instantiate it as such, otherwise we fallback to using hardwareMap.get()
        // if the device isn't a valid device at all (not a DeviceAccessor nor HardwareDevice) we just ignore it. this may actually happen pretty often depending on how the config is written
        for(cdevice in deviceArr) {
            if(cdevice.device != null && cdevice.name != null) {
                val device = cdevice.device!!
                val name = cdevice.name
                if(DeviceAccessor::class.java.isAssignableFrom(device.type)) {
                    device.set(Devices.Companion, device.type.getConstructor(String::class.java).newInstance(name))
                    println("$device from $Devices initialized by $name from ${HardwareGetter.hardwareMap}")
                }else if(HardwareDevice::class.java.isAssignableFrom(device.type)) {
                    device.set(Devices.Companion, HardwareGetter.hardwareMap!!.get(device.type, name))
                    println("$device from $Devices initialized by $name from ${HardwareGetter.hardwareMap}")
                }
            }
        }
    }
}
