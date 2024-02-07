package org.firstinspires.ftc.teamcode.internals.telemetry.logging

import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.Telemetry

object DashboardLogging {
    var TELEMETRY: Telemetry = FtcDashboard.getInstance().telemetry

    @JvmStatic
    fun update() {
        TELEMETRY.update()
    }

    @JvmStatic
    fun clear() {
        TELEMETRY.clear()
    }

    fun logData(key: String?, value: Any?) {
        TELEMETRY.addData(key, value)
    }

    fun logText(msg: String?) {
        TELEMETRY.addLine(msg)
    }

    fun log(msg: String?) {
        logText(msg)
    }

    @JvmStatic
    fun log(key: String?, value: Any?) {
        logData(key, value)
    }
}
