package org.firstinspires.ftc.teamcode.internals.custom_sensors;

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class MPU6050Test extends OperationMode implements TeleOperation {
    public MPU6050 mpu;
    @Override
    public void construct() {
        mpu = HardwareGetter.getHardwareMap().get(MPU6050.class, "arm_imu");
    }

    @Override
    public void run() {
        double[] accel = mpu.getAcceleration();
        double[] gyro = mpu.getRelativeOrientation();
        double[] absolutePitchRoll = mpu.getAbsoluteOrientation();
        // Log all three measurements
        Logging.log("ACCEL");
        Logging.log("     X", accel[0]);
        Logging.log("     Y", accel[1]);
        Logging.log("     Z", accel[2]);

        Logging.log("RELATIVE ORIENTATION");
        Logging.log("     X", gyro[0]);
        Logging.log("     Y", gyro[1]);
        Logging.log("     Z", gyro[2]);

        Logging.log("ABSOLUTE ORIENTATION");
        Logging.log("     Pitch", absolutePitchRoll[0]);
        Logging.log("     Roll", absolutePitchRoll[1]);
        Logging.update();
    }
}
