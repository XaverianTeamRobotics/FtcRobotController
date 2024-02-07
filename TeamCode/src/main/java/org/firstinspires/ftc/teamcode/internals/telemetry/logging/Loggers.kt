package org.firstinspires.ftc.teamcode.internals.telemetry.logging;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Loggers {

    private static boolean isInit = false;

    /**
     * Initializes all loggers if they haven't been initialized yet.
     */
    public static void init(Telemetry driver) {
        if(!isInit) {
            isInit = true;
            Logging.setTelemetry(driver);
            AdvancedLogging.setDriverTelemetry(driver);
            AdvancedLogging.getTelemetry();
            DSLogging.setTelemetry(driver);
        }
    }

}
