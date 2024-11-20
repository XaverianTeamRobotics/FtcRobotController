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

    public static MotorConfig DRIVE_FRONT_RIGHT = OdometrySettingStore.getMotor("DRIVE_FRONT_RIGHT");
    public static MotorConfig DRIVE_BACK_RIGHT = OdometrySettingStore.getMotor("DRIVE_BACK_RIGHT");
    public static MotorConfig DRIVE_FRONT_LEFT = OdometrySettingStore.getMotor("DRIVE_FRONT_LEFT");
    public static MotorConfig DRIVE_BACK_LEFT = OdometrySettingStore.getMotor("DRIVE_BACK_LEFT");
    public static String IMU_NAME = OdometrySettingStore.getIMU("IMU_NAME");
    public static double TICKS_PER_REV = OdometrySettingStore.getDouble("TICKS_PER_REV");
    public static double MAX_RPM = OdometrySettingStore.getDouble("MAX_RPM");
    public static double WHEEL_RADIUS = OdometrySettingStore.getDouble("WHEEL_RADIUS");
    public static double GEAR_RATIO = OdometrySettingStore.getDouble("GEAR_RATIO");
    public static double TRACK_WIDTH = OdometrySettingStore.getDouble("TRACK_WIDTH");
    public static double WHEEL_BASE = OdometrySettingStore.getDouble("WHEEL_BASE");
    public static double MAX_VEL = OdometrySettingStore.getDouble("MAX_VEL");
    public static double MAX_ACCEL = OdometrySettingStore.getDouble("MAX_ACCEL");
    public static double MAX_ANG_VEL = OdometrySettingStore.getDouble("MAX_ANG_VEL");
    public static double MAX_ANG_ACCEL = OdometrySettingStore.getDouble("MAX_ANG_ACCEL");
    public static double kA = OdometrySettingStore.getDouble("kA");
    public static double kV = OdometrySettingStore.getDouble("kV");
    public static double kStatic = OdometrySettingStore.getDouble("kStatic");
    public static double LATERAL_MULTIPLIER = OdometrySettingStore.getDouble("LATERAL_MULTIPLIER");
    public static PIDCoefficients HEADING_PID = OdometrySettingStore.getPID("HEADING_PID");
    public static PIDCoefficients TRANSLATIONAL_PID = OdometrySettingStore.getPID("TRANSLATIONAL_PID");
    public static double VX_WEIGHT = OdometrySettingStore.getDouble("VX_WEIGHT");
    public static double VY_WEIGHT = OdometrySettingStore.getDouble("VY_WEIGHT");
    public static double OMEGA_WEIGHT = OdometrySettingStore.getDouble("OMEGA_WEIGHT");
    public static double LIMELIGHT_MT_PIPELINE_ID = OdometrySettingStore.getDouble("LIMELIGHT_MT_PIPELINE_ID");
    public static double PINPOINT_X_OFFSET = OdometrySettingStore.getDouble("PINPOINT_X_OFFSET");
    public static double PINPOINT_Y_OFFSET = OdometrySettingStore.getDouble("PINPOINT_Y_OFFSET");
    public static boolean PINPOINT_X_REVERSED = OdometrySettingStore.getBoolean("PINPOINT_X_REVERSED");
    public static boolean PINPOINT_Y_REVERSED = OdometrySettingStore.getBoolean("PINPOINT_Y_REVERSED");
}
