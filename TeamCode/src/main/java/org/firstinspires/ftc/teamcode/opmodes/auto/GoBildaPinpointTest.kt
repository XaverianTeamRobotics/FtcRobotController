package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript
import org.firstinspires.ftc.teamcode.scripts.auto.GoBildaPinpointLogging

@TeleOp(group = BaseOpMode.AUTONOMOUS_GROUP_NAME)
class GoBildaPinpointTest: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript(rotScale = 0.75, powerScale = 0.75))
        addScript(GoBildaPinpointLogging())
    }

    override fun run() {

    }

    override fun onStop() {

    }
}