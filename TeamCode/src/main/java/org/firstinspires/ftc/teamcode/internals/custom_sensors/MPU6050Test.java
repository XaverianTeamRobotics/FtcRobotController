package org.firstinspires.ftc.teamcode.internals.custom_sensors;

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class MPU6050Test extends OperationMode implements TeleOperation {
    public MPU6050 mpu;
    public MPU6050AngularVelocityFilter filter;
    @Override
    public void construct() {
        mpu = HardwareGetter.getHardwareMap().get(MPU6050.class, "imu_6050");
        filter = new MPU6050AngularVelocityFilter();
    }

    @Override
    public void run() {
        double[] accel = mpu.getAcceleration();
        double[] gyro = mpu.getAngularVelocity();
        double[] orientation = filter.getOrientation(gyro);
        Logging.log("ACCEL");
        Logging.log("     X", accel[0]);
        Logging.log("     Y", accel[1]);
        Logging.log("     Z", accel[2]);

        Logging.log("ANGULAR VELOCITY");
        Logging.log("     X", gyro[0]);
        Logging.log("     Y", gyro[1]);
        Logging.log("     Z", gyro[2]);

        Logging.log("ORIENTATION");
        Logging.log("     PITCH", orientation[0]);
        Logging.log("     ROLL", orientation[1]);
        Logging.log("     YAW", orientation[2]);

        Logging.update();
    }
}
