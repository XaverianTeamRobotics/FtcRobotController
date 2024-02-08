package org.firstinspires.ftc.teamcode.internals.telemetry.logging

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.driverTelemetry
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging.Companion.telemetry
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.setTelemetry

object Loggers {
    private var isInit = false

    /**
     * Initializes all loggers if they haven't been initialized yet.
     */
    @JvmStatic
    fun init(driver: Telemetry?) {
        if (!isInit) {
            isInit = true
            Logging.setTelemetry(driver)
            driverTelemetry = driver!!
            telemetry
            setTelemetry(driver)
        }
    }
}
