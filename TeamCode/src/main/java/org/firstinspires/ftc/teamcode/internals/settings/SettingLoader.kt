package org.firstinspires.ftc.teamcode.internals.settings

import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.internal.system.AppUtil

object SettingLoader {
    /**
     * The path to the settings file.
     */
    val PATH: String = AppUtil.ROOT_FOLDER.toString() + "/odo7/settings.odo7"

    // i mean...technically we aren't using PATH for anything, but its still useful if we need it somewhere else i guess.
    private val PATH_INTERNAL = AppUtil.ROOT_FOLDER.toString() + "/odo7/"

    /**
     * Write to the odometry settings file.
     * @return true if the file was successfully written to, false if something went wrong
     */
    private fun write(): Boolean {
        try {
            // we need to do this ugliness because control hub os 1.1.3 uses android 7.1.1 which uses api 25 which doesnt support java.nio :(

            val dir: java.io.File = java.io.File(SettingLoader.PATH_INTERNAL)
            dir.mkdirs()
            val file: java.io.File = java.io.File(SettingLoader.PATH_INTERNAL + "settings.odo7")
            file.createNewFile()
            val fileWriter = java.io.FileWriter(file.getPath())

            // just making a string out of the current odo settings fields
            fileWriter.write(SettingLoader.makeString())
            fileWriter.close()
            return true
        } catch (exception: java.io.IOException) {
            exception.printStackTrace()
            return false
        }
    }

    private fun makeString(): kotlin.String? {
        val builder = java.lang.StringBuilder()
        builder.append(";")

        for (field in OdometrySettingsDashboardConfiguration::class.java.getFields()) {
            try {
                val str: kotlin.String? = SettingLoader.makeEntry(field)
                // Entries are delimited by a semicolon
                builder.append(str).append(";")
            } catch (exception: java.lang.IllegalAccessException) {
                kotlin.io.println("Failed to save field with exception '" + exception.message + "', moving on without saving this field.")
            }
        }

        // probably not necessary, but just in case
        if (!builder.toString().endsWith(";")) {
            builder.append(";")
        }

        return builder.toString()
    }

    /**
     * Entry syntax:
     * <blockquote>
     * Field name
     * <br></br>
     * Field type
     * <br></br>
     * Field value 1
     * <br></br>
     * Field value 2
     * <br></br>
     * Field value n...
     * <br></br>
    </blockquote> *
     * @throws IllegalAccessException whenever something goes wrong
     */
    @Throws(java.lang.IllegalAccessException::class)
    private fun makeEntry(field: java.lang.reflect.Field): kotlin.String? {
        val str = java.lang.StringBuilder()
        str.append(java.lang.System.lineSeparator()).append(field.getName()).append(java.lang.System.lineSeparator())

        // reflection
        // extracts the data from fields and converts it into a string
        val type: java.lang.Class<*>? = field.getType()
        if (type == kotlin.Double::class.javaPrimitiveType) {
            try {
                str.append("Double").append(java.lang.System.lineSeparator()).append(field.getDouble(null))
                    .append(java.lang.System.lineSeparator())
            } catch (e: java.lang.NullPointerException) {
                throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")
            }
        } else if (type == MotorConfig::class.java) {
            val config = field.get(null) as MotorConfig?
            if (config == null) throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")
            val direction: DcMotorSimple.Direction? = config.DIRECTION
            str.append("MotorConfig").append(java.lang.System.lineSeparator()).append(config.NAME)
                .append(java.lang.System.lineSeparator())

            if (direction == DcMotorSimple.Direction.FORWARD) {
                str.append("Forward")
            } else {
                str.append("Reverse")
            }

            str.append(java.lang.System.lineSeparator())
        } else if (type == EncoderConfig::class.java) {
            val config = field.get(null) as EncoderConfig?
            if (config == null) throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")
            val direction = config.DIRECTION
            str.append("EncoderConfig").append(java.lang.System.lineSeparator()).append(config.NAME)
                .append(java.lang.System.lineSeparator())

            if (direction == OdoEncoder.Direction.FORWARD) {
                str.append("Forward")
            } else {
                str.append("Reverse")
            }

            str.append(java.lang.System.lineSeparator())
        } else if (type == com.acmerobotics.roadrunner.control.PIDCoefficients::class.java) {
            val coefficients = field.get(null) as com.acmerobotics.roadrunner.control.PIDCoefficients?
            if (coefficients == null) throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")

            str.append("PIDCoefficents").append(java.lang.System.lineSeparator()).append(coefficients.kP)
                .append(java.lang.System.lineSeparator()).append(coefficients.kI)
                .append(java.lang.System.lineSeparator()).append(coefficients.kD)
                .append(java.lang.System.lineSeparator())
        } else if (type == kotlin.String::class.java) {
            val data = field.get(null) as kotlin.String?
            if (data == null) throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")

            str.append("IMU").append(java.lang.System.lineSeparator()).append(data)
                .append(java.lang.System.lineSeparator())
        } else if (type == LocalizationType::class.java) {
            val option = field.get(null) as LocalizationType?
            if (option == null) throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.")

            str.append("LocalizationType").append(java.lang.System.lineSeparator())
            if (option == LocalizationType.POD) {
                str.append("pod")
            } else {
                str.append("imu")
            }
            str.append(java.lang.System.lineSeparator())
        } else {
            throw java.lang.IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed.")
        }

        // finished "block" (data between 2 semicolons)
        //
        // kinda like this:
        //
        //      ;
        //      someSpeedValueIdk           // name of field
        //      DoubleArray                 // type
        //      0.0                         // first piece of data
        //      0.0                         // second piece of data
        //      0.0                         // third piece of data, etc...
        //      ;
        //
        return str.toString()
    }

    /**
     * Read from the odometry settings file.
     * @return the settings as a string, null if something went wrong
     */
    private fun read(): kotlin.String? {
        try {
            // again, api 25 ;-;

            val reader: java.io.FileReader = java.io.FileReader(SettingLoader.PATH_INTERNAL + "settings.odo7")
            val bufferedReader = java.io.BufferedReader(reader)
            val builder = java.lang.StringBuilder()

            var line: kotlin.String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                // oh. oh this second append call. it makes me LIVID.
                //                      |
                //                      V
                builder.append(line).append(java.lang.System.lineSeparator())
                //  i wrote nearly 1000 lines of reflection trying to fix a bug which i assumed was a reflection bug. it was Not a reflection bug. it was a formatting bug. i thought buffered readers kept the line seperator at the end of lines. i was wrong. very wrong
                // anyway, now we have an overengineered solution with a lot more reflection than necessary but im gonna keep it because it works..and it also prevents the bug i THOUGHT we were having from happening if com.acme.dashboard switches to building configs at build time rather than runtime
            }

            return builder.toString()
        } catch (exception: java.io.IOException) {
            exception.printStackTrace()
            return null
        }
    }

    /**
     * Generate a map of the currently stored settings.
     */
    private fun parseSettings(): java.util.HashMap<kotlin.String?, SettingLoader.Value?> {
        val file: kotlin.String? = SettingLoader.read()
        if (file != null) {
            // remember the reflection to convert this to text? now we do the opposite.

            val parsed = java.util.HashMap<kotlin.String?, SettingLoader.Value?>()
            val items = file.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (item in items) {
                var item = item
                item = item.trim { it <= ' ' }
                val pieces =
                    item.split(java.lang.System.lineSeparator().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var value: SettingLoader.Value?
                val trimmedPieces = java.util.ArrayList<kotlin.String?>()
                for (piece in pieces) {
                    trimmedPieces.add(piece.trim { it <= ' ' })
                }

                if (trimmedPieces.size < 2) {
                    continue
                }

                when (trimmedPieces.get(1)) {
                    "Double" -> value =
                        Value(trimmedPieces.get(2)!!.toDouble(), Double::class.java)

                    "Boolean" -> value =
                        Value(trimmedPieces.get(2)!!.toBoolean(), Boolean::class.java)

                    "MotorConfig" -> if (trimmedPieces.get(3) == "Forward") {
                        value = Value(
                            MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.FORWARD),
                            MotorConfig::class.java
                        )
                    } else {
                        value = Value(
                            MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.REVERSE),
                            MotorConfig::class.java
                        )
                    }

                    "EncoderConfig" -> if (trimmedPieces.get(3) == "Forward") {
                        value = Value(
                            EncoderConfig(trimmedPieces.get(2), OdoEncoder.Direction.FORWARD),
                            EncoderConfig::class.java
                        )
                    } else {
                        value = Value(
                            EncoderConfig(trimmedPieces.get(2), OdoEncoder.Direction.REVERSE),
                            EncoderConfig::class.java
                        )
                    }

                    "PIDCoefficents" -> value = Value(
                        com.acmerobotics.roadrunner.control.PIDCoefficients(
                            trimmedPieces.get(2)!!.toDouble(),
                            trimmedPieces.get(3)!!.toDouble(),
                            trimmedPieces.get(4)!!.toDouble()
                        ), com.acmerobotics.roadrunner.control.PIDCoefficients::class.java
                    )

                    "IMU" -> if (trimmedPieces.get(2) != null) {
                        value = Value(trimmedPieces.get(2)!!, kotlin.String::class.java)
                    } else {
                        value = Value("", kotlin.String::class.java)
                    }

                    "LocalizationType" -> if (trimmedPieces.get(2) == "pod") {
                        value = Value(LocalizationType.POD, LocalizationType::class.java)
                    } else {
                        value = Value(LocalizationType.IMU, LocalizationType::class.java)
                    }

                    else -> continue
                }
                parsed.put(trimmedPieces.get(0), value)
            }
            return parsed
        } else {
            throw SettingLoaderFailureException("Settings file not found!")
        }
    }

    /**
     * Save the current odometry settings.
     */
    @Throws(SettingLoaderFailureException::class)
    fun save() {
        try {
            if (!SettingLoader.write()) {
                throw SettingLoaderFailureException("Saving settings failed from IO exception. Check logcat for more details.")
            }
        } catch (e: java.lang.Exception) {
            throw SettingLoaderFailureException("Saving settings failed.", e)
        }
    }

    /**
     * Load the odometry settings from the most recent save.
     */
    @Throws(SettingLoaderFailureException::class)
    fun load(): java.util.HashMap<kotlin.String?, SettingLoader.Value?> {
        try {
            return SettingLoader.parseSettings()
        } catch (e: java.lang.Exception) {
            throw SettingLoaderFailureException("Loading settings failed.", e)
        }
    }

    class Value(`object`: kotlin.Any, clazz: java.lang.Class<*>?) {
        val obj: kotlin.Any
        val clazz: java.lang.Class<*>?

        init {
            obj = `object`
            this.clazz = clazz
        }

        override fun toString(): kotlin.String {
            return obj.toString() + " " + this.clazz
        }
    }
}
