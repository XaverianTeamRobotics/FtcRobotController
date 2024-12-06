package org.firstinspires.ftc.teamcode.autonomous.opmodes.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.autonomous.localizers.PinpointLocalizer
import org.firstinspires.ftc.teamcode.internals.settings.AutoSettings.PINPOINT_YAW_SCALAR
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode
import org.firstinspires.ftc.teamcode.scripts.MecanumDriveScript
import java.lang.Math.toDegrees

@TeleOp
class PinpointYawTuner: BaseOpMode() {
    override fun construct() {
        if (PINPOINT_YAW_SCALAR != 0.0) {
            throw RuntimeException("The PINPOINT_YAW_SCALAR must be 0 for this opmode")
        }
        addScript(MecanumDriveScript(powerScale = 0.25, rotScale = 0.75))
        telemetry.addLine("You will be asked to rotate the robot 1 full turn (360 degrees or 2PI radians)")
        telemetry.addLine("When done, press the A button. Press Play when ready")
        telemetry.update()
    }

    override fun run() {
        val localizer = PinpointLocalizer()
        localizer.update()
        val initial = localizer.poseEstimate.heading
        telemetry.addLine("Rotate the robot one full rotation, then press A")
        telemetry.update()

        while (!gamepad1.a && opModeIsActive()) { sleep(100) }

        localizer.update()
        val final = localizer.poseEstimate.heading
        val diff = toDegrees(final - initial)
        telemetry.addData("Difference (deg)", diff)
        val multiplier = 360 / diff
        telemetry.addData("Multiplier", multiplier)
        telemetry.update()
    }

    override fun onStop() {

    }
}