package org.firstinspires.ftc.teamcode.opmodes.hardwaretest

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.hardware.MagneticSensorSlideScript

@TeleOp(name = "Magnetic Sensor Slide", group = BaseOpMode.DEBUG_GROUP_NAME)
class MagneticSensorSlide: BaseOpMode() {
    override fun construct() {
        addScript(MagneticSensorSlideScript())
    }

    override fun run() {
    }

    override fun onStop() {
    }
}