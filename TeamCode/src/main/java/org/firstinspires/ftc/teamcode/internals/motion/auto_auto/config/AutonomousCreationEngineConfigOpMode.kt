package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.clear
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.log
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.update

class AutonomousCreationEngineConfigOpMode : OperationMode(), AutonomousOperation {
    override fun getNext(): Class<out OperationMode?>? {
        return null
    }

    private var hasSaved = false
    private lateinit var config: AutonomousCreationEngineConfig

    override fun construct() {
        config = AutonomousCreationEngineConfig()
        config.askQuestions()
        clear()
        log(config.toString())
        log("----------------")
        log(config.toJsonString())
        log("----------------")
        log("Press PLAY to save, or STOP to cancel")
        update()
    }

    override fun run() {
        if (!hasSaved) {
            log("Saving...")
            update()
            config.saveConfiguration()
            hasSaved = true
            log("Saved successfully")
            update()
        }
    }
}