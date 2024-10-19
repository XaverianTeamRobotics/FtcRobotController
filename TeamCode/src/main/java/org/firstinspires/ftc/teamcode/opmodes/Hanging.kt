package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.base.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.HangingScript

@TeleOp(name = "Hanging", group = BaseOpMode.DEBUG_GROUP_NAME)
class Hanging: BaseOpMode() {
    override fun construct() {
        addScript(HangingScript())
    }

    override fun run() {
    }

    override fun onStop() {
    }
}