package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class Intake<motorPower> extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        double motorPower = Devices.motor0.getPower();
        Devices.motor0.setPower(100);
        if (Devices.controller1.getDpadUp()) {
            if (motorPower == 100) {
                Devices.motor0.setPower(100);
            }
            if (motorPower < 100) {
                Devices.motor0.setPower(motorPower - 10);
                if (motorPower <= 0) {
                    Devices.motor0.setPower(0);
                }
            }
        }
    }
}
