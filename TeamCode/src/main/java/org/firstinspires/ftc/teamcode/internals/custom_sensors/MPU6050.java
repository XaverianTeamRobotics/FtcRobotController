package org.firstinspires.ftc.teamcode.internals.custom_sensors;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

@I2cDeviceType()
@DeviceProperties(name = "Adafruit MPU-6050", xmlTag = "MPU6050", description = "Adafruit MPU-6050 6-DoF Accelerometer and Gyroscope")
public class MPU6050 extends I2cDeviceSynchDevice<I2cDeviceSynch> implements HardwareDevice {
    private static final int ACC_2G = 0;
    private static final int ACC_4G = 1;
    private static final int ACC_8G = 2;
    private static final int ACC_16G = 3;

    private static final int GYRO_250DPS = 0;
    private static final int GYRO_500DPS = 1;
    private static final int GYRO_1000DPS = 2;
    private static final int GYRO_2000DPS = 3;

    private double[] orientation = new double[3]; // Pitch, Roll, Yaw
    private double lastTimeOfOrientationMeasurement;

    public MPU6050(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);

        this.deviceClient.setI2cAddress(I2cAddr.create7bit(0x68));

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
        lastTimeOfOrientationMeasurement = System.currentTimeMillis();
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

    /**
     * Get the three-axis acceleration data from the sensor
     * @return an array of three doubles containing the acceleration data, in g. X, Y, and Z
     * X - left/right
     * Y - forward/backward
     * Z - up/down
     */
    public double[] getAcceleration() {
        double[] data = getData();
        double rawAccX = data[0];
        double rawAccY = data[1];
        double rawAccZ = data[2];

        double accScale = getAccelScale();

        double accX = rawAccX / (accScale);
        double accY = rawAccY / (accScale);
        double accZ = rawAccZ / (accScale);

        return new double[] { accX, accY, accZ };
    }

    /**
     * Find the sensor's angular velocity
     * @return an array of three doubles containing the gyroscope data, in degrees per second. X, Y, and Z
     * X - pitch
     * Y - roll
     * Z - yaw
     */
    public double[] getAngularVelocity() {
        double[] data = getData();
        double rawGyroX = data[4];
        double rawGyroY = data[5];
        double rawGyroZ = data[6];

        double gyroScale = getGyroScale();

        double gyroX = rawGyroX / (gyroScale);
        double gyroY = rawGyroY / (gyroScale);
        double gyroZ = rawGyroZ / (gyroScale);

        return new double[] { gyroX, gyroY, gyroZ };
    }

    private void setAccelScale(int scale) {
        writeShort(Register.ACCEL_CONFIG, (short) (scale << 3));
    }

    private double getAccelScale() {
        int accelCode = read2Registers(Register.ACCEL_CONFIG) >> 3;
        switch (accelCode) {
            case ACC_2G:
                return 2048;
            case ACC_4G:
                return 4096;
            case ACC_8G:
                return 8192;
            case ACC_16G:
                return 16384;
            default:
                throw new RuntimeException("Invalid accelerometer scale code");
        }
    }

    private void setGyroScale(int scale) {
        writeShort(Register.GYRO_CONFIG, (short) (scale << 3));
    }

    private double getGyroScale() {
        int gyroCode = read2Registers(Register.GYRO_CONFIG) >> 3;
        switch (gyroCode) {
            case GYRO_250DPS:
                return 131;
            case GYRO_500DPS:
                return 65.5;
            case GYRO_1000DPS:
                return 32.8;
            case GYRO_2000DPS:
                return 16.4;
            default:
                throw new RuntimeException("Invalid gyroscope scale code");
        }
    }

    private double[] getData() {
        double[] data = new double[7];
        byte[] buffer = deviceClient.read(Register.ACCEL_XOUT.bVal, 14);
        data[0] = TypeConversion.byteArrayToShort(new byte[] { buffer[0], buffer[1] });     // X acceleration
        data[1] = TypeConversion.byteArrayToShort(new byte[] { buffer[2], buffer[3] });     // Y acceleration
        data[2] = TypeConversion.byteArrayToShort(new byte[] { buffer[4], buffer[5] });     // Z acceleration
        data[3] = TypeConversion.byteArrayToShort(new byte[] { buffer[6], buffer[7] });     // Temperature
        data[4] = TypeConversion.byteArrayToShort(new byte[] { buffer[8], buffer[9] });     // X gyro
        data[5] = TypeConversion.byteArrayToShort(new byte[] { buffer[10], buffer[11] });   // Y gyro
        data[6] = TypeConversion.byteArrayToShort(new byte[] { buffer[12], buffer[13] });   // Z gyro
        return data;
	}

    protected short read2Registers(Register reg) {
        return TypeConversion.byteArrayToShort(deviceClient.read(reg.bVal, 2));
    }

    protected float read2RegistersFloat(Register reg) {
        // Take the first register and use it as the high byte, and the second register as the "decimal" byte
        return (float) (TypeConversion.byteArrayToShort(deviceClient.read(reg.bVal, 2)) / 16384.0);
    }

    protected void writeShort(Register reg, short data) {
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
        ACCEL_XOUT(0x3B),
        ACCEL_YOUT(0x3D),
        ACCEL_ZOUT(0x3F),
        TEMP_OUT(0x41),
        GYRO_XOUT(0x43),
        GYRO_YOUT(0x45),
        GYRO_ZOUT(0x47),
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