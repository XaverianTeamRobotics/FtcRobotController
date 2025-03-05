package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.TiltAndExtendScript

@TeleOp
class TiltAndExtendOpmode: BaseOpMode() {
    override fun construct() {
        addScript(TiltAndExtendScript(tiltMaxVel = 3000.0, extendMaxVel = 3000.0, extend2MaxVel = 3000.0, gamepadEnable = true))
    }

    override fun run() {

    }

    override fun onStop() {

    }
}