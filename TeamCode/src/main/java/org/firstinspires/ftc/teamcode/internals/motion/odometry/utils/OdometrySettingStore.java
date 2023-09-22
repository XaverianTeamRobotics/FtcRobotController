package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

/**
 * Manages the state of {@link OdometrySettingsDashboardConfiguration} fields before being loaded into the dashboard.
 */
public class OdometrySettingStore {

    private static boolean isMade;
    private static boolean isOkay = true;
    private static HashMap<String, SettingLoader.Value> vals;

    /**
     * Prepares odometry settings to be loaded into the dashboard.
     */
    public static void init() {
        // we want to do this to force this.vals to be regenrtated on app load even after a soft restart
        isMade = false;
    }

    /**
     * Imports the current configuration from a file or from {@link OdometrySettings}.
     */
    private static void makeConfig() {
        // we also want to make sure this never gets regenerated AFTER app load...that would be very bad
        if(!isMade) {
            vals = getDefaults();
            try {
                HashMap<String, SettingLoader.Value> xvals = SettingLoader.load();
                vals.putAll(xvals);
            } catch(SettingLoaderFailureException e) {
                e.printStackTrace();
                isOkay = false;
            }
        }
    }

    /**
     * Get the default values to whatever the user set beforehand.
     */
    private static HashMap<String, SettingLoader.Value> getDefaults() {
        // the defaults are stored in DefaultOdometrySettings, so we use some basic reflection to put them into the hashmap
        // calling reflection basic is a crime punishible to the highest degree but i work with it so much im getting good at it now... D:
        HashMap<String, SettingLoader.Value> kvals = new HashMap<>();
        for(Field field : OdometrySettings.class.getDeclaredFields()) {
            try {
                kvals.put(field.getName(), new SettingLoader.Value(field.get(null), field.getType()));
            } catch(IllegalAccessException e) {
                e.printStackTrace();
                System.out.println(field + " failed to be added to the default odometry settings map, skipping...");
            }
        }
        return kvals;
    }

    public static MotorConfig getMotor(String name) {
        makeConfig();
        return (MotorConfig) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static EncoderConfig getEncoder(String name) {
        makeConfig();
        return (EncoderConfig) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static double getDouble(String name) {
        makeConfig();
        return (double) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static PIDCoefficients getPID(String name) {
        makeConfig();
        return (PIDCoefficients) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static String getIMU(String name) {
        makeConfig();
        return (String) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static LocalizationType getType(String name) {
        makeConfig();
        return (LocalizationType) Objects.requireNonNull(vals.get(name)).obj;
    }

    public static boolean isOkay() {
        return isOkay;
    }

}
