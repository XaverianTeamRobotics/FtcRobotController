package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Servo;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class MotorTest extends OperationMode implements TeleOperation {

    private int servoNum;
    private boolean squareP, crossP, rbP, lbP;

    public Servo getServo(int num) {
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
        if (num > 4) {
            servoNum = 4;
            return null;
        }
        servoNum = 0;
        return null;
    }

    @Override
    public void construct() {
        servoNum = 0;
        Devices.servo0.setPosition(0);
        squareP = false;
        crossP = false;
    }

    @Override
    public void run() {
        double temp = getServo(0).getPosition();
        if (!squareP) temp -= Devices.controller1.getSquare() ? 10 : 0;
        if (!crossP) temp += Devices.controller1.getCross() ? 10 : 0;
        if (!rbP) servoNum += Devices.controller1.getRightBumper() ? 1 : 0;
        if (!lbP) servoNum -= Devices.controller1.getLeftBumper() ? 0 : 1;
        if (temp > 100) {
            temp = 100.0;
        }
        if (temp < 0) {
            temp = 0.0;
        }
        squareP = Devices.controller1.getSquare();
        crossP = Devices.controller1.getCross();
        rbP = Devices.controller1.getRightBumper();
        lbP = Devices.controller1.getLeftBumper();

        Logging.log("Servo", servoNum);
        Logging.log("Total", temp);
        Logging.update();
        getServo(servoNum).setPosition(temp);
    }
}
