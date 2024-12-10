package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.PinpointLoggingScript

@TeleOp(group = BaseOpMode.AUTONOMOUS_GROUP_NAME)
class PinpointLogging: BaseOpMode() {
    override fun construct() {
        addScript(PinpointLoggingScript())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}