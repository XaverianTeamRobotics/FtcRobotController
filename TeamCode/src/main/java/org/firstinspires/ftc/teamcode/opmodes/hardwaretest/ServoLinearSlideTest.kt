package org.firstinspires.ftc.teamcode.opmodes.hardwaretest

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.hardware.ContinuousServoScript

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