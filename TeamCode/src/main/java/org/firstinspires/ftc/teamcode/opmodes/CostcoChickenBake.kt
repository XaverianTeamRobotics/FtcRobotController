package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript

@Autonomous(preselectTeleOp = "VectorBot")
class CostcoChickenBake: BaseOpMode() {
    private var input: Double = 1.0
    override fun construct() {
        telemetry.addLine("Hello, World!")
        telemetry.update()
        for (i in 0..3) {
            motors[i].zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }
        addScript(MecanumDriveScript(forwardInput = {-input/10.0}, rotationInput = {0.0}, lateralInput = {input}))
    }

    override fun run() {
        sleep(1750)
        input = 0.0
        requestOpModeStop()
    }

    override fun onStop() {
        input = 0.0
    }
}