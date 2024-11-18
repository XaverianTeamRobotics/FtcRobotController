package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript

@TeleOp(group = BaseOpMode.DEBUG_GROUP_NAME)
class DualServoIntakeTest : BaseOpMode() {
    override fun construct() {
        addScript(ContinuousServoScript(id = 0, inverted = true))
        addScript(ContinuousServoScript(id = 1, inverted = false))
    }

    override fun run() {

    }

    override fun onStop() {

    }

}