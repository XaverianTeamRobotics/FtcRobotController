package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.huskylens
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.ContinuousServoScript
import org.firstinspires.ftc.teamcode.scripts.HuskylensRotateScript

@TeleOp(name = "HuskyLensRotateClaw", group = BaseOpMode.DEBUG_GROUP_NAME)
class HuskyLensRotateClaw: BaseOpMode() {
    override fun construct() {
        addScript(HuskylensRotateScript())
        addScript(ContinuousServoScript(id = 0, input = { HuskylensRotateScript.getAngle(
            huskylens.arrows()[0].x_origin.toDouble(),
            huskylens.arrows()[0].y_origin.toDouble(),
            huskylens.arrows()[0].x_target.toDouble(),
            huskylens.arrows()[0].y_target.toDouble()) }))
    }

    override fun run() {
    }

    override fun onStop() {
    }
}