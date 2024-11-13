package org.firstinspires.ftc.teamcode.internals.settings;

import android.content.Context;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.ftccommon.external.OnCreate;

import java.util.Arrays;

@Config(value = "OdometrySettings")
public class OdometrySettingsDashboardConfiguration {


    /**


     +----------------------------------------------------------------------------------------------------------------------+
     |                                                                                                                      |
     |                                                      !!STOP!!                                                        |
     |                                                                                                                      |
     |   If you're going to edit the odometry settings file in the source code, go edit {@link OdometrySettings} instead!   |
     |   (also dont even try making this kotlin)                                                                            |
     +----------------------------------------------------------------------------------------------------------------------+


     */

    @OnCreate
    public static void start(Context context) {
        try {
            FtcDashboard.stop(context);
            OdometrySettingStore.init();

            // force restarting by requiring dash instance to be null
            FtcDashboard.start(context);

            if (!OdometrySettingStore.isOkay()) {
                System.out.println("Autonomous settings not OK. See other logs.");
                RobotLog.addGlobalWarningMessage("Autonomous settings failed to load from the most recent save! Does a save exist? Check logcat for more details.");
            } else {
                System.out.println("Autonomous settings OK.");
            }
        } catch (Exception e) {
            RobotLog.e(e.toString());
            RobotLog.addGlobalWarningMessage("Autonomous settings failed to load! Check logcat for more details.");
        }
    }

    public static double LIMELIGHT_MT_PIPELINE_ID = OdometrySettingStore.getDouble("LIMELIGHT_MT_PIPELINE_ID");
    public static double PINPOINT_X_OFFSET = OdometrySettingStore.getDouble("PINPOINT_X_OFFSET");
    public static double PINPOINT_Y_OFFSET = OdometrySettingStore.getDouble("PINPOINT_Y_OFFSET");
}
