package org.firstinspires.ftc.teamcode.features

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging

class CurrentMonitoringFeature : Feature(), Buildable {
    lateinit var motor0: DcMotorEx
    lateinit var motor1: DcMotorEx
    lateinit var motor2: DcMotorEx
    lateinit var motor3: DcMotorEx
    override fun build() {
        motor0 = HardwareGetter.hardwareMap!!.get(DcMotorEx::class.java, "motor0")
        motor1 = HardwareGetter.hardwareMap!!.get(DcMotorEx::class.java, "motor1")
        motor2 = HardwareGetter.hardwareMap!!.get(DcMotorEx::class.java, "motor2")
        motor3 = HardwareGetter.hardwareMap!!.get(DcMotorEx::class.java, "motor3")
    }

    override fun loop() {
        Logging.log("Motor 0 current", motor0.getCurrent(CurrentUnit.MILLIAMPS))
        Logging.log("Motor 1 current", motor1.getCurrent(CurrentUnit.MILLIAMPS))
        Logging.log("Motor 2 current", motor2.getCurrent(CurrentUnit.MILLIAMPS))
        Logging.log("Motor 3 current", motor3.getCurrent(CurrentUnit.MILLIAMPS))

        Logging.update()
    }
}