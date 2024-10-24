package org.firstinspires.ftc.teamcode.internals.settings;

import android.content.Context;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.ftccommon.external.OnCreate;

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
        FtcDashboard.stop(context);
        OdometrySettingStore.init();

        // force restarting by requiring dash instance to be null
        FtcDashboard.start(context);

        if(!OdometrySettingStore.isOkay()) {
            System.out.println("Odometry settings not OK. See other logs.");
            RobotLog.addGlobalWarningMessage("Odometry settings failed to load from the most recent save! Does a save exist? Check logcat for more details.");
        }else{
            System.out.println("Odometry settings OK.");
        }
    }

    public static LocalizationType LOCALIZATION_TYPE = OdometrySettingStore.getType("LOCALIZATION_TYPE");
    public static MotorConfig DRIVE_FRONT_RIGHT = OdometrySettingStore.getMotor("DRIVE_FRONT_RIGHT");
    public static MotorConfig DRIVE_BACK_RIGHT = OdometrySettingStore.getMotor("DRIVE_BACK_RIGHT");
    public static MotorConfig DRIVE_FRONT_LEFT = OdometrySettingStore.getMotor("DRIVE_FRONT_LEFT");
    public static MotorConfig DRIVE_BACK_LEFT = OdometrySettingStore.getMotor("DRIVE_BACK_LEFT");
    public static EncoderConfig ENCODER_RIGHT = OdometrySettingStore.getEncoder("ENCODER_RIGHT");
    public static EncoderConfig ENCODER_LEFT = OdometrySettingStore.getEncoder("ENCODER_LEFT");
    public static EncoderConfig ENCODER_MIDDLE = OdometrySettingStore.getEncoder("ENCODER_MIDDLE");
    public static String IMU_NAME = OdometrySettingStore.getIMU("IMU");
    public static double TICKS_PER_REV = OdometrySettingStore.getDouble("TICKS_PER_REV");
    public static double MAX_RPM = OdometrySettingStore.getDouble("MAX_RPM");
    public static double WHEEL_RADIUS = OdometrySettingStore.getDouble("WHEEL_RADIUS");
    public static double GEAR_RATIO = OdometrySettingStore.getDouble("GEAR_RATIO");
    public static double TRACK_WIDTH = OdometrySettingStore.getDouble("TRACK_WIDTH");
    public static double MAX_VEL = OdometrySettingStore.getDouble("MAX_VEL");
    public static double MAX_ACCEL = OdometrySettingStore.getDouble("MAX_ACCEL");
    public static double MAX_ANG_VEL = OdometrySettingStore.getDouble("MAX_ANG_VEL");
    public static double MAX_ANG_ACCEL = OdometrySettingStore.getDouble("MAX_ANG_ACCEL");
    public static double ENCODER_TICKS_PER_REV = OdometrySettingStore.getDouble("ENCODER_TICKS_PER_REV");
    public static double ENCODER_WHEEL_RADIUS = OdometrySettingStore.getDouble("ENCODER_WHEEL_RADIUS");
    public static double ENCODER_GEAR_RATIO = OdometrySettingStore.getDouble("ENCODER_GEAR_RATIO");
    public static double ENCODER_TRACK_WIDTH = OdometrySettingStore.getDouble("ENCODER_TRACK_WIDTH");
    public static double ENCODER_FORWARD_OFFSET = OdometrySettingStore.getDouble("ENCODER_FORWARD_OFFSET");
    public static double kA = OdometrySettingStore.getDouble("kA");
    public static double kV = OdometrySettingStore.getDouble("kV");
    public static double kStatic = OdometrySettingStore.getDouble("kStatic");
    public static double LATERAL_MULTIPLIER = OdometrySettingStore.getDouble("LATERAL_MULTIPLIER");
    public static PIDCoefficients HEADING_PID = OdometrySettingStore.getPID("HEADING_PID");
    public static PIDCoefficients TRANSLATIONAL_PID = OdometrySettingStore.getPID("TRANSLATIONAL_PID");
    public static double VX_WEIGHT = OdometrySettingStore.getDouble("VX_WEIGHT");
    public static double VY_WEIGHT = OdometrySettingStore.getDouble("VY_WEIGHT");
    public static double OMEGA_WEIGHT = OdometrySettingStore.getDouble("OMEGA_WEIGHT");

}
