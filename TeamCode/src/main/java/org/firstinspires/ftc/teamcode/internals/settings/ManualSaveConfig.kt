package org.firstinspires.ftc.teamcode.internals.settings

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.internals.templates.BaseOpMode

@TeleOp(name = "Manually Save Auto Settings", group = BaseOpMode.DEBUG_GROUP_NAME)
class ManualSaveConfig : BaseOpMode() {
    private var saved = false

    public override fun construct() {
    }

    public override fun run() {
        while (true) {
            if (!saved) {
                saved = true
                telemetry.clear()
                telemetry.update()
                telemetry.addLine("Saving...")
                telemetry.update()
                try {
                    java.lang.Thread.sleep(1000)
                } catch (`_`: java.lang.Exception) {
                }
                telemetry.clear()
                telemetry.update()
                try {
                    SettingLoader.save()
                    telemetry.addLine("Saved. Exiting OpMode...")
                    telemetry.update()
                    try {
                        java.lang.Thread.sleep(1000)
                    } catch (`_`: java.lang.Exception) {
                    }
                    requestOpModeStop()
                } catch (e: SettingLoaderFailureException) {
                    telemetry.addLine("Saving settings failed! " + e.message)
                    telemetry.update()
                }
            }
        }
    }

    public override fun onStop() {
    }
}
