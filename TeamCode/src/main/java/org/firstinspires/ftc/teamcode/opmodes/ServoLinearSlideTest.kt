package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript


@TeleOp(name = "Servo Linear Slide Test", group = BaseOpMode.DEBUG_GROUP_NAME)
class ServoLinearSlideTest : BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}