package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning;

import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoader;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoaderFailureException;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class ManuallySaveOdo extends OperationMode implements TeleOperation {

    private boolean saved = false;

    @Override
    public void construct() {

    }

    @Override
    public void run() {
        if(!saved) {
            saved = true;
            DSLogging.clear();
            DSLogging.update();
            DSLogging.log("Saving...");
            DSLogging.update();
            Clock.sleep(1000);
            DSLogging.clear();
            DSLogging.update();
            try {
                SettingLoader.save();
                DSLogging.log("Saved. Exiting OpMode...");
                DSLogging.update();
                Clock.sleep(1000);
                requestOpModeStop();
            } catch(SettingLoaderFailureException e) {
                System.out.println("Saving settings failed! " + e.getMessage());
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
    }
}
