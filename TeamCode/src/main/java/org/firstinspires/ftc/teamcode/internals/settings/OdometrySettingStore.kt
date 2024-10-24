package org.firstinspires.ftc.teamcode.internals.settings

import com.acmerobotics.roadrunner.control.PIDCoefficients
import java.util.HashMap
import java.util.Objects

/**
 * Manages the state of [OdometrySettingsDashboardConfiguration] fields before being loaded into the dashboard.
 */
object OdometrySettingStore {
    private var isMade = false
    private var isOkay = true
    private var vals: HashMap<String?, SettingLoader.Value?>? = null

    /**
     * Prepares odometry settings to be loaded into the dashboard.
     */
    @JvmStatic
    fun init() {
        // we want to do this to force this.vals to be regenrtated on app load even after a soft restart
        isMade = false
    }

    /**
     * Imports the current configuration from a file or from [OdometrySettings].
     */
    private fun makeConfig() {
        // we also want to make sure this never gets regenerated AFTER app load...that would be very bad
        if (!isMade) {
            vals = getDefaults()
            try {
                val xvals = SettingLoader.load()
                vals!!.putAll(xvals)
            } catch (e: SettingLoaderFailureException) {
                e.printStackTrace()
                isOkay = false
            }
        }
    }

    /**
     * Get the default values to whatever the user set beforehand.
     */
    private fun getDefaults(): HashMap<String?, SettingLoader.Value?> {
        // the defaults are stored in DefaultOdometrySettings, so we use some basic reflection to put them into the hashmap
        // calling reflection basic is a crime punishible to the highest degree but i work with it so much im getting good at it now... D:
        val kvals = HashMap<String?, SettingLoader.Value?>()
        for (field in OdometrySettings::class.java.getDeclaredFields()) {
            try {
                kvals.put(field.getName(), SettingLoader.Value(field.get(null), field.getType()))
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                println(field.toString() + " failed to be added to the default odometry settings map, skipping...")
            }
        }
        return kvals
    }

    @JvmStatic
    fun getMotor(name: String?): MotorConfig? {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as MotorConfig?
    }

    @JvmStatic
    fun getEncoder(name: String?): EncoderConfig? {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as EncoderConfig?
    }

    @JvmStatic
    fun getDouble(name: String?): Double {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as Double
    }

    @JvmStatic
    fun getPID(name: String?): PIDCoefficients? {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as PIDCoefficients?
    }

    @JvmStatic
    fun getIMU(name: String?): String? {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as String?
    }

    @JvmStatic
    fun getType(name: String?): LocalizationType? {
        makeConfig()
        return Objects.requireNonNull<SettingLoader.Value?>(vals!!.get(name)).obj as LocalizationType?
    }

    @JvmStatic
    fun isOkay(): Boolean {
        return isOkay
    }
}
