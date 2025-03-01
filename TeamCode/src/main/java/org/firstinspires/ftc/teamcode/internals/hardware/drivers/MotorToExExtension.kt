package org.firstinspires.ftc.teamcode.internals.hardware.drivers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

fun DcMotor.isEx(): Boolean {
    return this is DcMotorEx
}

fun DcMotor.toEx(): DcMotorEx {
    return this as DcMotorEx
}