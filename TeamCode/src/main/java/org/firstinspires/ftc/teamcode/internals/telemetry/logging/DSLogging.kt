package org.firstinspires.ftc.teamcode.internals.telemetry.logging

import org.firstinspires.ftc.robotcore.external.Telemetry

object DSLogging {
    var TELEMETRY: Telemetry? = null

    @JvmStatic
    fun setTelemetry(driver: Telemetry?) {
        TELEMETRY = driver
    }

    @JvmStatic
    fun update() {
        TELEMETRY!!.update()
    }

    @JvmStatic
    fun clear() {
        TELEMETRY!!.clear()
    }

    fun logData(key: String?, value: Any?) {
        TELEMETRY!!.addData(key, value)
    }

    fun logText(msg: String?) {
        TELEMETRY!!.addLine(msg)
    }

    @JvmStatic
    fun log(msg: String?) {
        logText(msg)
    }

    @JvmStatic
    fun log(key: String?, value: Any?) {
        logData(key, value)
    }
}
