package org.firstinspires.ftc.teamcode.internals.telemetry.logging

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.robotcore.external.Telemetry

object Logging {
    var TELEMETRY: MultipleTelemetry? = null
    private var telemetry: Telemetry? = null

    fun setTelemetry(driver: Telemetry?) {
        TELEMETRY = MultipleTelemetry(driver, FtcDashboard.getInstance().telemetry)
        telemetry = driver
    }

    @JvmStatic
    fun update() {
        TELEMETRY!!.update()
    }

    @JvmStatic
    fun clear() {
        TELEMETRY!!.clear()
    }

    @JvmStatic
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

    @JvmStatic
    fun setAutoClear(autoClear: Boolean) {
        telemetry!!.isAutoClear = autoClear
    }
}
