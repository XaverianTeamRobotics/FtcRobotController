package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Servo;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class ServoTest extends OperationMode implements TeleOperation {

    private int servoNum, lastServo;
    private boolean squareP, crossP, rbP, lbP;

    public Servo getServo() {
        Servo temp = getServo(servoNum);
        if (lastServo != servoNum) temp.setPosition(0);
        lastServo = servoNum;
        return temp;
    }

    public Servo getServo(int num) {
        if (num > 5) {
            servoNum = 5;
            return null;
        }
        if (num < 0) {
            servoNum = 0;
        }
        if (num == 0) {
            return Devices.getServo0();
        }
        if (num == 1) {
            return Devices.getServo1();
        }
        if (num == 2) {
            return Devices.getServo2();
        }
        if (num == 3) {
            return Devices.getServo3();
        }
        if (num == 4) {
            return Devices.getServo4();
        }
        if (num == 5) {
            return Devices.getServo5();
        }
        return null;
    }

    @Override
    public void construct() {
        servoNum = 0;
        squareP = false;
        crossP = false;
        rbP = false;
        lbP = false;
    }

    @Override
    public void run() {
        double temp = getServo().getPosition();
        if (!squareP) temp -= Devices.controller1.getSquare() ? 10 : 0;
        if (!crossP) temp += Devices.controller1.getCross() ? 10 : 0;
        if (!rbP) servoNum += Devices.controller1.getRightBumper() ? 1 : 0;
        if (!lbP) servoNum -= Devices.controller1.getLeftBumper() ? 1 : 0;
        temp = Math.max(0, Math.min(100, temp));
        squareP = Devices.controller1.getSquare();
        crossP = Devices.controller1.getCross();
        rbP = Devices.controller1.getRightBumper();
        lbP = Devices.controller1.getLeftBumper();

        if (getServo() != null) {
            getServo().setPosition(temp);
        }
        Logging.log("Servo", servoNum);
        Logging.log("Total", temp);
        Logging.update();
    }
}
