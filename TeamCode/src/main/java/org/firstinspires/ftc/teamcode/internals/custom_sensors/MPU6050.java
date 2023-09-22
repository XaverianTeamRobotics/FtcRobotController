package org.firstinspires.ftc.teamcode.internals.custom_sensors;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.ArrayList;

import static java.lang.Math.*;

@I2cDeviceType()
@DeviceProperties(name = "Adafruit MPU-6050", xmlTag = "MPU6050", description = "Adafruit MPU-6050 6-DoF Accelerometer and Gyroscope")
public class MPU6050 extends I2cDeviceSynchDevice<I2cDeviceSynch> implements HardwareDevice {
    public ArrayList<double[]> accelHistory = new ArrayList<>();

    public MPU6050(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);

        this.deviceClient.setI2cAddress(I2cAddr.create7bit(0x68));

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    protected boolean doInitialize() {
        // Do the necessary initialization to get the sensor working
        // Set the clock source to the internal oscillator
        writeShort(Register.PWR_MGMT_1, (short) 0x00);
        // Set the gyro range to 250 degrees per second
        writeShort(Register.GYRO_CONFIG, (short) 0x00);
        // Set the accelerometer range to 2g
        writeShort(Register.ACCEL_CONFIG, (short) 0x00);
        // Set the sample rate to 1kHz
        writeShort(Register.SMPLRT_DIV, (short) 0x00);
        // Enable the accelerometer and gyro
        writeShort(Register.PWR_MGMT_1, (short) 0x00);
        // Enable the FIFO
        writeShort(Register.USER_CTRL, (short) 0x40);
        // Enable the I2C master
        writeShort(Register.USER_CTRL, (short) 0x20);
        // Set the I2C master clock speed to 400kHz
        writeShort(Register.I2C_MST_CTRL, (short) 0x0D);
        // Enable the data ready interrupt
        writeShort(Register.INT_ENABLE, (short) 0x01);
        // Enable the interrupt
        writeShort(Register.INT_PIN_CFG, (short) 0x02);

        return true;
    }
    
    // Create some basic functions to read the sensor data from the registers
    // Note that most values are 16-bit and in 2's complement format

    /**
     * Get the three-axis acceleration data from the sensor
     * @return an array of three doubles containing the acceleration data, in g. X, Y, and Z
     * X - left/right
     * Y - forward/backward
     * Z - up/down
     */
    public double[] getAcceleration() {
        double[] accel = new double[3];
        accel[0] = read2RegistersFloat(Register.ACCEL_XOUT_H);
        accel[1] = read2RegistersFloat(Register.ACCEL_YOUT_H);
        accel[2] = read2RegistersFloat(Register.ACCEL_ZOUT_H);
        double[] timestampedAccel = new double[4];
        timestampedAccel[0] = accel[0];
        timestampedAccel[1] = accel[1];
        timestampedAccel[2] = accel[2];
        timestampedAccel[3] = System.currentTimeMillis();
        accelHistory.add(timestampedAccel);
        if (accelHistory.size() > 1000) {
            accelHistory.remove(0);
        }
        return accel;
    }

    /**
     * Get a noise-corrected acceleration value
     * Note: Because of the algorithm used, this may lag behind rapid changes in acceleration
     * The algorith is an average of the past 100 ms of acceleration data
     * @return an array of three doubles containing the acceleration data, in g. X, Y, and Z
     * X - left/right
     * Y - forward/backward
     * Z - up/down
     */
    public double[] getAccelerationCorrected() {
        double[] accel = new double[3];
        double[] accelSum = new double[3];
        for (double[] accelData : accelHistory) {
            if (accelData[3] - System.currentTimeMillis() > 100) {
                continue;
            }
            accelSum[0] += accelData[0];
            accelSum[1] += accelData[1];
            accelSum[2] += accelData[2];
        }
        accel[0] = accelSum[0] / accelHistory.size();
        accel[1] = accelSum[1] / accelHistory.size();
        accel[2] = accelSum[2] / accelHistory.size();
        return accel;
    }

    /**
     * Find the sensor's orientation relative to itself
     * @return an array of three doubles containing the gyroscope data, in degrees per second. X, Y, and Z
     * X - pitch
     * Y - roll
     * Z - yaw
     */
    public double[] getRelativeOrientation() {
        double[] gyro = new double[3];
        gyro[0] = read2Registers(Register.GYRO_XOUT_H);
        gyro[1] = read2Registers(Register.GYRO_YOUT_H);
        gyro[2] = read2Registers(Register.GYRO_ZOUT_H);
        return gyro;
    }

    /**
     * Find the sensor's orientation relative to gravity.
     * Sensor must remain stationary for accurate results
     * @return an array of two doubles containing the pitch and roll angles relative to earth, in degrees. Pitch then Roll
     */
    public double[] getAbsoluteOrientation() {
        double[] accel = getAcceleration();
        double[] pitchRoll = new double[2];
        pitchRoll[0] = atan2(accel[1], sqrt(pow(accel[0], 2) + pow(accel[2], 2)));
        pitchRoll[1] = atan2(accel[0], sqrt(pow(accel[1], 2) + pow(accel[2], 2)));
        return pitchRoll;
    }

    protected short read2Registers(Register reg)
    {
        return TypeConversion.byteArrayToShort(deviceClient.read(reg.bVal, 2));
    }

    protected float read2RegistersFloat(Register reg)
    {
        // Take the first register and use it as the high byte, and the second register as the "decimal" byte
        return (float) (TypeConversion.byteArrayToShort(deviceClient.read(reg.bVal, 2)) / 16384.0);
    }

    protected void writeShort(Register reg, short data)
    {
        deviceClient.write8(reg.bVal, data);
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Adafruit;
    }

    @Override
    public String getDeviceName() {
        return "Adafruit MPU-6050";
    }

    public enum Register {
        WHO_AM_I(0x75),
        PWR_MGMT_1(0x6B),
        CONFIG(0x1A),
        GYRO_CONFIG(0x1B),
        ACCEL_CONFIG(0x1C),
        ACCEL_XOUT_H(0x3B),
        ACCEL_XOUT_L(0x3C),
        ACCEL_YOUT_H(0x3D),
        ACCEL_YOUT_L(0x3E),
        ACCEL_ZOUT_H(0x3F),
        ACCEL_ZOUT_L(0x40),
        TEMP_OUT_H(0x41),
        TEMP_OUT_L(0x42),
        GYRO_XOUT_H(0x43),
        GYRO_XOUT_L(0x44),
        GYRO_YOUT_H(0x45),
        GYRO_YOUT_L(0x46),
        GYRO_ZOUT_H(0x47),
        GYRO_ZOUT_L(0x48),
        SMPLRT_DIV(0x19),
        INT_ENABLE(0x38),
        INT_STATUS(0x3A),
        USER_CTRL(0x6A),
        I2C_MST_CTRL(0x24),
        INT_PIN_CFG(0x37);

        public final byte bVal;

        Register(int i) {
            this.bVal = (byte) i;
        }
    }
}