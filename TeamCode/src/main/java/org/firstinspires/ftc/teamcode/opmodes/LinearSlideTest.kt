package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousMotorScript


@TeleOp(name = "Linear Slide Test", group = BaseOpMode.DEBUG_GROUP_NAME)
class LinearSlideTest : BaseOpMode() {
    override fun construct() {
        addScript(ContinuousMotorScript())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}