package org.firstinspires.ftc.teamcode.autonomous.limelight

import org.firstinspires.ftc.teamcode.autonomous.localizers.LimelightLocalizer
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings
import org.firstinspires.ftc.teamcode.internals.templates.Script

class LimelightServoScript(val pos: () -> LimelightServoPosition) : Script() {
    val servo = HardwareManager.servos.get("ll-servo", 0)

    override fun init() {
        servo.position = pos().value()
    }

    override fun run() {
        while (scriptIsActive()) {
            servo.position = pos().value()
            LimelightLocalizer.disabled = pos() != LimelightServoPosition.CENTER
            LimelightLocalizer.servoPos = pos()
        }
    }

    override fun onStop() {

    }

    enum class LimelightServoPosition(val value: () -> Double) {
        CENTER(AutoSettings::LIMELIGHT_SERVO_CENTER), BUCKET(AutoSettings::LIMELIGHT_SERVO_BUCKET)
    }
}