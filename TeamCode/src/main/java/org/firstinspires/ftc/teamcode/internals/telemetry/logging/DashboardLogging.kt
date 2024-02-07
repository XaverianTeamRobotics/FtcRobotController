package org.firstinspires.ftc.teamcode.internals.telemetry.logging;

import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DashboardLogging {

    public static Telemetry TELEMETRY = FtcDashboard.getInstance().getTelemetry();

    public static void update() {
        TELEMETRY.update();
    }

    public static void clear() {
        TELEMETRY.clear();
    }

    public static void logData(String key, Object value) {
        TELEMETRY.addData(key, value);
    }

    public static void logText(String msg) {
        TELEMETRY.addLine(msg);
    }

    public static void log(String msg) {
        logText(msg);
    }

    public static void log(String key, Object value) {
        logData(key, value);
    }

}
