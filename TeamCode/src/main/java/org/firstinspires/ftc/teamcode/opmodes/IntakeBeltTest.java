package org.firstinspires.ftc.teamcode.opmodes;

import android.bluetooth.BluetoothClass;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class IntakeBeltTest extends OperationMode implements TeleOperation {

    double intakeSpeed = 0;
    double intakeSensitivity = .0002;
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        intakeSpeed += intakeSensitivity * Devices.controller1.getRightTrigger();
        intakeSpeed -= intakeSensitivity * Devices.controller1.getLeftTrigger();
        if(Devices.controller1.getLeftBumper()) {
            intakeSpeed = 0;
        }
        intakeSpeed = Math.min(100, intakeSpeed);
        intakeSpeed = Math.max(-100, intakeSpeed);
        Devices.getMotor0().setPower(intakeSpeed);
    }
}
