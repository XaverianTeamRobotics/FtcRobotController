package org.firstinspires.ftc.teamcode.internals.settings;

import android.content.Context;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.ftccommon.external.OnCreate;

@Config(value = "AutoSettings")
public class AutoSettings {


    /**


     +----------------------------------------------------------------------------------------------------------------------+
     |                                                                                                                      |
     |                                                      !!STOP!!                                                        |
     |                                                                                                                      |
     |   If you're going to edit the odometry settings file in the source code, go edit {@link DefaultAutoSettings} instead!   |
     |   (also dont even try making this kotlin)                                                                            |
     +----------------------------------------------------------------------------------------------------------------------+


     */

    @OnCreate
    public static void start(Context context) {
        reloadDashboard(context);
    }

    public static void reloadDashboard(Context context) {
        try {
            FtcDashboard.stop(context);
            AutoSettingsStore.init();

            // force restarting by requiring dash instance to be null
            FtcDashboard.start(context);

            if (!AutoSettingsStore.isOkay()) {
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

    public static MotorConfig DRIVE_FRONT_RIGHT = AutoSettingsStore.getMotor("DRIVE_FRONT_RIGHT");
    public static MotorConfig DRIVE_BACK_RIGHT = AutoSettingsStore.getMotor("DRIVE_BACK_RIGHT");
    public static MotorConfig DRIVE_FRONT_LEFT = AutoSettingsStore.getMotor("DRIVE_FRONT_LEFT");
    public static MotorConfig DRIVE_BACK_LEFT = AutoSettingsStore.getMotor("DRIVE_BACK_LEFT");
    public static String IMU_NAME = AutoSettingsStore.getIMU("IMU_NAME");
    public static double TICKS_PER_REV = AutoSettingsStore.getDouble("TICKS_PER_REV");
    public static double MAX_RPM = AutoSettingsStore.getDouble("MAX_RPM");
    public static double WHEEL_RADIUS = AutoSettingsStore.getDouble("WHEEL_RADIUS");
    public static double GEAR_RATIO = AutoSettingsStore.getDouble("GEAR_RATIO");
    public static double TRACK_WIDTH = AutoSettingsStore.getDouble("TRACK_WIDTH");
    public static double WHEEL_BASE = AutoSettingsStore.getDouble("WHEEL_BASE");
    public static double MAX_VEL = AutoSettingsStore.getDouble("MAX_VEL");
    public static double MAX_ACCEL = AutoSettingsStore.getDouble("MAX_ACCEL");
    public static double MAX_ANG_VEL = AutoSettingsStore.getDouble("MAX_ANG_VEL");
    public static double MAX_ANG_ACCEL = AutoSettingsStore.getDouble("MAX_ANG_ACCEL");
    public static double kA = AutoSettingsStore.getDouble("kA");
    public static double kV = AutoSettingsStore.getDouble("kV");
    public static double kStatic = AutoSettingsStore.getDouble("kStatic");
    public static double LATERAL_MULTIPLIER = AutoSettingsStore.getDouble("LATERAL_MULTIPLIER");
    public static PIDCoefficients HEADING_PID = AutoSettingsStore.getPID("HEADING_PID");
    public static PIDCoefficients TRANSLATIONAL_PID = AutoSettingsStore.getPID("TRANSLATIONAL_PID");
    public static double VX_WEIGHT = AutoSettingsStore.getDouble("VX_WEIGHT");
    public static double VY_WEIGHT = AutoSettingsStore.getDouble("VY_WEIGHT");
    public static double OMEGA_WEIGHT = AutoSettingsStore.getDouble("OMEGA_WEIGHT");
    public static double LIMELIGHT_MT_PIPELINE_ID = AutoSettingsStore.getDouble("LIMELIGHT_MT_PIPELINE_ID");
    public static double PINPOINT_X_OFFSET = AutoSettingsStore.getDouble("PINPOINT_X_OFFSET");
    public static double PINPOINT_Y_OFFSET = AutoSettingsStore.getDouble("PINPOINT_Y_OFFSET");
    public static boolean PINPOINT_X_REVERSED = AutoSettingsStore.getBoolean("PINPOINT_X_REVERSED");
    public static boolean PINPOINT_Y_REVERSED = AutoSettingsStore.getBoolean("PINPOINT_Y_REVERSED");
    public static double MAX_SAFE_ANGULAR_VELOCITY = AutoSettingsStore.getDouble("MAX_SAFE_ANGULAR_VELOCITY");
    public static double MAX_SAFE_LINEAR_VELOCITY = AutoSettingsStore.getDouble("MAX_SAFE_LINEAR_VELOCITY");

}
