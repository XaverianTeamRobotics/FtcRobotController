package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo0
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo2
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo3
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo4
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.servo5
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Servo
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import kotlin.math.max
import kotlin.math.min

class ServoTest : OperationMode(), TeleOperation {
    private var servoNum = 0
    private var lastServo = 0
    private var squareP = false
    private var crossP = false
    private var rbP = false
    private var lbP = false

    val servo: Servo?
        get() {
            val temp = getServo(servoNum)
            if (lastServo != servoNum) temp!!.position = 0.0
            lastServo = servoNum
            return temp
        }

    fun getServo(num: Int): Servo? {
        if (num > 5) {
            servoNum = 5
            return null
        }
        if (num < 0) {
            servoNum = 0
        }
        if (num == 0) {
            return servo0
        }
        if (num == 1) {
            return servo1
        }
        if (num == 2) {
            return servo2
        }
        if (num == 3) {
            return servo3
        }
        if (num == 4) {
            return servo4
        }
        if (num == 5) {
            return servo5
        }
        return null
    }

    override fun construct() {
        servoNum = 0
        squareP = false
        crossP = false
        rbP = false
        lbP = false
    }

    override fun run() {
        var temp = servo!!.position
        if (!squareP) temp -= (if (Devices.controller1.square) 10 else 0).toDouble()
        if (!crossP) temp += (if (Devices.controller1.cross) 10 else 0).toDouble()
        if (!rbP) servoNum += if (Devices.controller1.rightBumper) 1 else 0
        if (!lbP) servoNum -= if (Devices.controller1.leftBumper) 1 else 0
        temp = max(0.0, min(100.0, temp))
        squareP = Devices.controller1.square
        crossP = Devices.controller1.cross
        rbP = Devices.controller1.rightBumper
        lbP = Devices.controller1.leftBumper

        if (servo != null) {
            servo!!.position = temp
        }
        Logging.log("Servo", servoNum)
        Logging.log("Total", temp)
        Logging.update()
    }
}
