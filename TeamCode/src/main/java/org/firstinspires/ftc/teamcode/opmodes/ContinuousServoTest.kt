package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager

@TeleOp(name = "Continuous Servo Test", group = BaseOpMode.DEBUG_GROUP_NAME)
class ContinuousServoTest : BaseOpMode() {
    override fun construct() {

    }

    override fun run() {
        HardwareManager.servos[0].position = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()
    }

    override fun onStop() {

    }
}