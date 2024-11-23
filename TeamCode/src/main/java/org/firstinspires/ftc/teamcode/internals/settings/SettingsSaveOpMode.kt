package org.firstinspires.ftc.teamcode.internals.settings

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode.Companion.AUTONOMOUS_GROUP_NAME

@TeleOp(group = AUTONOMOUS_GROUP_NAME)
class SettingsSaveOpMode: BaseOpMode() {
    override fun construct() {
        SettingLoader.save()
        telemetry.addLine("Settings saved")
        telemetry.update()
    }

    override fun run() {

    }

    override fun onStop() {

    }
}