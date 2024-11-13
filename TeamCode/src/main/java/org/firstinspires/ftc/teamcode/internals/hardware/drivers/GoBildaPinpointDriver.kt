package org.firstinspires.ftc.teamcode.internals.hardware.drivers

import com.qualcomm.hardware.lynx.LynxI2cDeviceSynch
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.I2cAddr
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType
import com.qualcomm.robotcore.util.TypeConversion
import com.qualcomm.robotcore.util.TypeConversion.byteArrayToInt
import org.firstinspires.ftc.teamcode.autonomous.localization.RobotPose
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.DeviceStatus
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.GoBildaOdometryPods
import org.firstinspires.ftc.teamcode.internals.hardware.drivers.GoBildaPinpointDriver.readData
import org.firstinspires.ftc.teamcode.internals.units.mm
import org.firstinspires.ftc.teamcode.internals.units.radians
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Arrays

@I2cDeviceType
@DeviceProperties(
    name = "goBILDA® Pinpoint Odometry Computer",
    xmlTag = "goBILDAPinpoint",
    description = "goBILDA® Pinpoint Odometry Computer (IMU Sensor Fusion for 2 Wheel Odometry)"
)
/**
 * The goBILDA Pinpoint Odometry Computer is a device that uses two odometry pods to track the position of a robot on the field.
 *
 * *This code was made by goBILDA, not the team, and adapted for Kotlin.
 * [The original code can be found here](https://github.com/goBILDA-Official/FtcRobotController-Add-Pinpoint/blob/goBILDA-Odometry-Driver/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/GoBildaPinpointDriver.java)*
 */
class GoBildaPinpointDriver(deviceClient: I2cDeviceSynchSimple, deviceClientIsOwned: Boolean) :
    I2cDeviceSynchDevice<I2cDeviceSynchSimple>(deviceClient, deviceClientIsOwned) {
    var deviceStatus = 0
        private set
    var loopTime = 0
        private set
    var xEncoderValue = 0
        private set
    var yEncoderValue = 0
        private set
    var xPosition = 0.0
        private set
    var yPosition = 0.0
        private set
    var hOrientation = 0.0
        private set
    var xVelocity = 0.0
        private set
    var yVelocity = 0.0
        private set
    var hVelocity = 0.0
        private set

    init {
        this.deviceClient?.i2cAddress = I2cAddr.create7bit(DEFAULT_ADDRESS.toInt())
        super.registerArmingStateCallback(false)
    }


    override fun getManufacturer(): Manufacturer {
        return Manufacturer.Other
    }

    @Synchronized
    protected override fun doInitialize(): Boolean {
        ((deviceClient) as LynxI2cDeviceSynch).setBusSpeed(LynxI2cDeviceSynch.BusSpeed.FAST_400K)
        return true
    }

    override fun getDeviceName(): String {
        return "goBILDA® Pinpoint Odometry Computer"
    }

    //Register map of the i2c device
    private enum class Register(val bVal: Int) {
        DEVICE_ID(1),
        DEVICE_VERSION(2),
        DEVICE_STATUS(3),
        DEVICE_CONTROL(4),
        LOOP_TIME(5),
        X_ENCODER_VALUE(6),
        Y_ENCODER_VALUE(7),
        X_POSITION(8),
        Y_POSITION(9),
        H_ORIENTATION(10),
        X_VELOCITY(11),
        Y_VELOCITY(12),
        H_VELOCITY(13),
        MM_PER_TICK(14),
        X_POD_OFFSET(15),
        Y_POD_OFFSET(16),
        YAW_SCALAR(17),
        BULK_READ(18);
    }

    //Device Status enum that captures the current fault condition of the device
    enum class DeviceStatus(val status: Int) {
        NOT_READY(0),
        READY(1),
        CALIBRATING(1 shl 1),
        FAULT_X_POD_NOT_DETECTED(1 shl 2),
        FAULT_Y_POD_NOT_DETECTED(1 shl 3),
        FAULT_NO_PODS_DETECTED(1 shl 2 or (1 shl 3)),
        FAULT_IMU_RUNAWAY(1 shl 4);

    }

    //enum that captures the direction the encoders are set to
    enum class EncoderDirection {
        FORWARD,
        REVERSED
    }

    //enum that captures the kind of goBILDA odometry pods, if goBILDA pods are used
    enum class GoBildaOdometryPods {
        goBILDA_SWINGARM_POD,
        goBILDA_4_BAR_POD
    }

    //enum that captures a limited scope of read data. More options may be added in future update
    enum class readData {
        ONLY_UPDATE_HEADING,
    }


    /** Writes an int to the i2c device
     * @param reg the register to write the int to
     * @param i the integer to write to the register
     */
    private fun writeInt(reg: Register, i: Int) {
        deviceClient.write(reg.bVal, TypeConversion.intToByteArray(i, ByteOrder.LITTLE_ENDIAN))
    }

    /**
     * Reads an int from a register of the i2c device
     * @param reg the register to read from
     * @return returns an int that contains the value stored in the read register
     */
    private fun readInt(reg: Register): Int {
        return byteArrayToInt(deviceClient.read(reg.bVal, 4), ByteOrder.LITTLE_ENDIAN)
    }

    /**
     * Converts a byte array to a float value
     * @param byteArray byte array to transform
     * @param byteOrder order of byte array to convert
     * @return the float value stored by the byte array
     */
    private fun byteArrayToFloat(byteArray: ByteArray, byteOrder: ByteOrder): Float {
        return ByteBuffer.wrap(byteArray).order(byteOrder).getFloat()
    }

    /**
     * Reads a float from a register
     * @param reg the register to read
     * @return the float value stored in that register
     */
    private fun readFloat(reg: Register): Float {
        return byteArrayToFloat(deviceClient.read(reg.bVal, 4), ByteOrder.LITTLE_ENDIAN)
    }


    /**
     * Converts a float to a byte array
     * @param value the float array to convert
     * @return the byte array converted from the float
     */
    private fun floatToByteArray(value: Float, byteOrder: ByteOrder): ByteArray {
        return ByteBuffer.allocate(4).order(byteOrder).putFloat(value).array()
    }

    /**
     * Writes a byte array to a register on the i2c device
     * @param reg the register to write to
     * @param bytes the byte array to write
     */
    private fun writeByteArray(reg: Register, bytes: ByteArray?) {
        deviceClient.write(reg.bVal, bytes)
    }

    /**
     * Writes a float to a register on the i2c device
     * @param reg the register to write to
     * @param f the float to write
     */
    private fun writeFloat(reg: Register, f: Float) {
        val bytes: ByteArray? = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(f).array()
        deviceClient.write(reg.bVal, bytes)
    }

    /**
     * Looks up the DeviceStatus enum corresponding with an int value
     * @param s int to lookup
     * @return the Odometry Computer state
     */
    private fun lookupStatus(s: Int): DeviceStatus {
        if ((s and DeviceStatus.CALIBRATING.status) != 0) {
            return DeviceStatus.CALIBRATING
        }
        val xPodDetected = (s and DeviceStatus.FAULT_X_POD_NOT_DETECTED.status) == 0
        val yPodDetected = (s and DeviceStatus.FAULT_Y_POD_NOT_DETECTED.status) == 0

        if (!xPodDetected && !yPodDetected) {
            return DeviceStatus.FAULT_NO_PODS_DETECTED
        }
        if (!xPodDetected) {
            return DeviceStatus.FAULT_X_POD_NOT_DETECTED
        }
        if (!yPodDetected) {
            return DeviceStatus.FAULT_Y_POD_NOT_DETECTED
        }
        if ((s and DeviceStatus.FAULT_IMU_RUNAWAY.status) != 0) {
            return DeviceStatus.FAULT_IMU_RUNAWAY
        }
        if ((s and DeviceStatus.READY.status) != 0) {
            return DeviceStatus.READY
        } else {
            return DeviceStatus.NOT_READY
        }
    }

    /**
     * Call this once per loop to read new data from the Odometry Computer. Data will only update once this is called.
     */
    fun update() {
        val bArr: ByteArray = deviceClient.read(Register.BULK_READ.bVal, 40)
        deviceStatus = byteArrayToInt(bArr.copyOfRange(0, 4), ByteOrder.LITTLE_ENDIAN)
        loopTime = byteArrayToInt(bArr.copyOfRange(4, 8), ByteOrder.LITTLE_ENDIAN)
        xEncoderValue = byteArrayToInt(bArr.copyOfRange(8, 12), ByteOrder.LITTLE_ENDIAN)
        yEncoderValue = byteArrayToInt(bArr.copyOfRange(12, 16), ByteOrder.LITTLE_ENDIAN)
        xPosition = byteArrayToFloat(bArr.copyOfRange(16, 20), ByteOrder.LITTLE_ENDIAN).toDouble()
        yPosition = byteArrayToFloat(bArr.copyOfRange(20, 24), ByteOrder.LITTLE_ENDIAN).toDouble()
        hOrientation = byteArrayToFloat(bArr.copyOfRange(24, 28), ByteOrder.LITTLE_ENDIAN).toDouble()
        xVelocity = byteArrayToFloat(bArr.copyOfRange(28, 32), ByteOrder.LITTLE_ENDIAN).toDouble()
        yVelocity = byteArrayToFloat(bArr.copyOfRange(32, 36), ByteOrder.LITTLE_ENDIAN).toDouble()
        hVelocity = byteArrayToFloat(bArr.copyOfRange(36, 40), ByteOrder.LITTLE_ENDIAN).toDouble()
    }

    /**
     * Call this once per loop to read new data from the Odometry Computer. This is an override of the update() function
     * which allows a narrower range of data to be read from the device for faster read times. Currently ONLY_UPDATE_HEADING
     * is supported.
     * @param data GoBildaPinpointDriver.readData.ONLY_UPDATE_HEADING
     */
    fun update(data: readData?) {
        if (data == readData.ONLY_UPDATE_HEADING) {
            hOrientation = byteArrayToFloat(deviceClient.read(Register.H_ORIENTATION.bVal, 4), ByteOrder.LITTLE_ENDIAN).toDouble()
        }
    }

    /**
     * Sets the odometry pod positions relative to the point that the odometry computer tracks around.<br></br><br></br>
     * The most common tracking position is the center of the robot. <br></br> <br></br>
     * The X pod offset refers to how far sideways (in mm) from the tracking point the X (forward) odometry pod is. Left of the center is a positive number, right of center is a negative number. <br></br>
     * the Y pod offset refers to how far forwards (in mm) from the tracking point the Y (strafe) odometry pod is. forward of center is a positive number, backwards is a negative number.<br></br>
     * @param xOffset how sideways from the center of the robot is the X (forward) pod? Left increases
     * @param yOffset how far forward from the center of the robot is the Y (Strafe) pod? forward increases
     */
    fun setOffsets(xOffset: Double, yOffset: Double) {
        writeFloat(Register.X_POD_OFFSET, xOffset.toFloat())
        writeFloat(Register.Y_POD_OFFSET, yOffset.toFloat())
    }

    /**
     * Recalibrates the Odometry Computer's internal IMU. <br></br><br></br>
     * ** Robot MUST be stationary ** <br></br><br></br>
     * Device takes a large number of samples, and uses those as the gyroscope zero-offset. This takes approximately 0.25 seconds.
     */
    fun recalibrateIMU() {
        writeInt(Register.DEVICE_CONTROL, 1 shl 0)
    }

    /**
     * Resets the current position to 0,0,0 and recalibrates the Odometry Computer's internal IMU. <br></br><br></br>
     * ** Robot MUST be stationary ** <br></br><br></br>
     * Device takes a large number of samples, and uses those as the gyroscope zero-offset. This takes approximately 0.25 seconds.
     */
    fun resetPosAndIMU() {
        writeInt(Register.DEVICE_CONTROL, 1 shl 1)
    }

    /**
     * Can reverse the direction of each encoder.
     * @param xEncoder FORWARD or REVERSED, X (forward) pod should increase when the robot is moving forward
     * @param yEncoder FORWARD or REVERSED, Y (strafe) pod should increase when the robot is moving left
     */
    fun setEncoderDirections(xEncoder: EncoderDirection?, yEncoder: EncoderDirection?) {
        if (xEncoder == EncoderDirection.FORWARD) {
            writeInt(Register.DEVICE_CONTROL, 1 shl 5)
        }
        if (xEncoder == EncoderDirection.REVERSED) {
            writeInt(Register.DEVICE_CONTROL, 1 shl 4)
        }

        if (yEncoder == EncoderDirection.FORWARD) {
            writeInt(Register.DEVICE_CONTROL, 1 shl 3)
        }
        if (yEncoder == EncoderDirection.REVERSED) {
            writeInt(Register.DEVICE_CONTROL, 1 shl 2)
        }
    }

    /**
     * If you're using goBILDA odometry pods, the ticks-per-mm values are stored here for easy access.<br></br><br></br>
     * @param pods goBILDA_SWINGARM_POD or goBILDA_4_BAR_POD
     */
    fun setEncoderResolution(pods: GoBildaOdometryPods?) {
        if (pods == GoBildaOdometryPods.goBILDA_SWINGARM_POD) {
            writeByteArray(Register.MM_PER_TICK, (floatToByteArray(goBILDA_SWINGARM_POD, ByteOrder.LITTLE_ENDIAN)))
        }
        if (pods == GoBildaOdometryPods.goBILDA_4_BAR_POD) {
            writeByteArray(Register.MM_PER_TICK, (floatToByteArray(goBILDA_4_BAR_POD, ByteOrder.LITTLE_ENDIAN)))
        }
    }

    /**
     * Sets the encoder resolution in ticks per mm of the odometry pods. <br></br>
     * You can find this number by dividing the counts-per-revolution of your encoder by the circumference of the wheel.
     * @param ticks_per_mm should be somewhere between 10 ticks/mm and 100 ticks/mm a goBILDA Swingarm pod is ~13.26291192
     */
    fun setEncoderResolution(ticks_per_mm: Double) {
        writeByteArray(Register.MM_PER_TICK, (floatToByteArray(ticks_per_mm.toFloat(), ByteOrder.LITTLE_ENDIAN)))
    }

    /**
     * Tuning this value should be unnecessary.<br></br>
     * The goBILDA Odometry Computer has a per-device tuned yaw offset already applied when you receive it.<br></br><br></br>
     * This is a scalar that is applied to the gyro's yaw value. Increasing it will mean it will report more than one degree for every degree the sensor fusion algorithm measures. <br></br><br></br>
     * You can tune this variable by rotating the robot a large amount (10 full turns is a good starting place) and comparing the amount that the robot rotated to the amount measured.
     * Rotating the robot exactly 10 times should measure 3600°. If it measures more or less, divide moved amount by the measured amount and apply that value to the Yaw Offset.<br></br><br></br>
     * If you find that to get an accurate heading number you need to apply a scalar of more than 1.05, or less than 0.95, your device may be bad. Please reach out to tech@gobilda.com
     * @param yawOffset A scalar for the robot's heading.
     */
    fun setYawScalar(yawOffset: Double) {
        writeByteArray(Register.YAW_SCALAR, (floatToByteArray(yawOffset.toFloat(), ByteOrder.LITTLE_ENDIAN)))
    }

    /**
     * Send a position that the Pinpoint should use to track your robot relative to. You can use this to
     * update the estimated position of your robot with new external sensor data, or to run a robot
     * in field coordinates. <br></br><br></br>
     * This overrides the current position. <br></br><br></br>
     * **Using this feature to track your robot's position in field coordinates:** <br></br>
     * When you start your code, send a Pose2D that describes the starting position on the field of your robot. <br></br>
     * Say you're on the red alliance, your robot is against the wall and closer to the audience side,
     * and the front of your robot is pointing towards the center of the field.
     * You can send a setPosition with something like -600mm x, -1200mm Y, and 90 degrees. The pinpoint would then always
     * keep track of how far away from the center of the field you are. <br></br><br></br>
     * **Using this feature to update your position with additional sensors: **<br></br>
     * Some robots have a secondary way to locate their robot on the field. This is commonly
     * Apriltag localization in FTC, but it can also be something like a distance sensor.
     * Often these external sensors are absolute (meaning they measure something about the field)
     * so their data is very accurate. But they can be slower to read, or you may need to be in a very specific
     * position on the field to use them. In that case, spend most of your time relying on the Pinpoint
     * to determine your location. Then when you pull a new position from your secondary sensor,
     * send a setPosition command with the new position. The Pinpoint will then track your movement
     * relative to that new, more accurate position.
     * @param pos a Pose2D describing the robot's new position.
     */
    fun setPosition(pos: RobotPose): RobotPose {
        writeByteArray(
            Register.X_POSITION,
            (floatToByteArray(pos.x.mm.toFloat(), ByteOrder.LITTLE_ENDIAN))
        )
        writeByteArray(
            Register.Y_POSITION,
            (floatToByteArray(pos.y.mm.toFloat(), ByteOrder.LITTLE_ENDIAN))
        )
        writeByteArray(
            Register.H_ORIENTATION,
            (floatToByteArray(pos.heading.radians.toFloat(), ByteOrder.LITTLE_ENDIAN))
        )
        return pos
    }

    /**
     * Checks the deviceID of the Odometry Computer. Should return 1.
     * @return 1 if device is functional.
     */
    fun getDeviceID(): Int {
        return readInt(Register.DEVICE_ID)
    }

    /**
     * @return the firmware version of the Odometry Computer
     */
    fun getDeviceVersion(): Int {
        return readInt(Register.DEVICE_VERSION)
    }

    fun getYawScalar(): Float {
        return readFloat(Register.YAW_SCALAR)
    }

    /**
     * Device Status stores any faults the Odometry Computer may be experiencing. These faults include:
     * @return one of the following states:<br></br>
     * NOT_READY - The device is currently powering up. And has not initialized yet. RED LED<br></br>
     * READY - The device is currently functioning as normal. GREEN LED<br></br>
     * CALIBRATING - The device is currently recalibrating the gyro. RED LED<br></br>
     * FAULT_NO_PODS_DETECTED - the device does not detect any pods plugged in. PURPLE LED <br></br>
     * FAULT_X_POD_NOT_DETECTED - The device does not detect an X pod plugged in. BLUE LED <br></br>
     * FAULT_Y_POD_NOT_DETECTED - The device does not detect a Y pod plugged in. ORANGE LED <br></br>
     */
    fun getDeviceStatus(): DeviceStatus {
        return lookupStatus(deviceStatus)
    }

    /**
     * Checks the Odometry Computer's most recent loop frequency.<br></br><br></br>
     * If values less than 900, or more than 2000 are commonly seen here, there may be something wrong with your device. Please reach out to tech@gobilda.com
     * @return Pinpoint Frequency in Hz (loops per second),
     */
    fun getFrequency(): Double {
        if (loopTime != 0) {
            return 1000000.0 / loopTime
        } else {
            return 0.0
        }
    }

    /**
     * ** This uses its own I2C read, avoid calling this every loop. **
     * @return the user-set offset for the X (forward) pod
     */
    val getXOffset: Float
        get() = readFloat(Register.X_POD_OFFSET)

    /**
     * ** This uses its own I2C read, avoid calling this every loop. **
     * @return the user-set offset for the Y (strafe) pod
     */
    val yOffset: Float
        get() = readFloat(Register.Y_POD_OFFSET)

    /**
     * @return a Pose containing the estimated position of the robot
     */
    val position: RobotPose
        get() = RobotPose(
            xPosition.mm,
            yPosition.mm,
            hOrientation.radians
        )


    /**
     * @return a Pose2D containing the estimated velocity of the robot, velocity is unit per second
     */
    val velocity: RobotPose
        get() = RobotPose(
            xVelocity.mm,
            yVelocity.mm,
            hVelocity.radians
        )

    companion object {
        private const val goBILDA_SWINGARM_POD = 13.26291192f //ticks-per-mm for the goBILDA Swingarm Pod
        private const val goBILDA_4_BAR_POD = 19.89436789f //ticks-per-mm for the goBILDA 4-Bar Pod

        //i2c address of the device
        const val DEFAULT_ADDRESS: Byte = 0x31
    }
}
