package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript

@TeleOp(name = "Full power motor", group = BaseOpMode.DEBUG_GROUP_NAME)
class FullPowerMotor: BaseOpMode() {
    override fun construct() {
        addScript(ContinuousMotorScript(id = 0, input = { 1.0 }))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}