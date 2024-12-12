/**
 * Dremel is a simple configuration file format that allows for easy configuration of robot settings.
 * It is designed to be human editable and easy to read.
 * Here is a basic format of a Dremel file:
 *
 * ```
 * name : type : value
 * ```
 *
 * Comments can be added by using the `/` character.
 *
 * ```
 * / This is a comment
 * ```
 *
 * _**(IMPORTANT)**_ A comment must be on its own line.
 *
 * Supported types:
 * - int
 * - double
 * - boolean
 * - string
 */
class DremelInterpreter(raw: String) {
    private val lines = raw.split("\n")
    private val settings = mutableMapOf<String, Any>()

    init {
        for (line in lines) {
            if (line.startsWith("/"))  {
                continue
            } else {
                val parts = line.split(" : ")
                if (parts.size < 3) continue
                val name = parts[0].trim()
                val type = parts[1].trim()
                val value = parts[2].trim()
                if (type == "int" && value.toIntOrNull().toString() != value) {
                    throw IllegalArgumentException("Invalid value for int type: $value")
                } else if (type == "double" && value.toDoubleOrNull().toString() != value) {
                    throw IllegalArgumentException("Invalid value for double type: $value")
                } else if (type == "boolean" && value != "true" && value != "false") {
                    throw IllegalArgumentException("Invalid value for boolean type: $value")
                }
                settings[name] = when (type) {
                    "int" -> value.toInt()
                    "double" -> value.toDouble()
                    "boolean" -> value.toBoolean()
                    else -> value
                }
            }
        }
    }


    fun getSetting(name: String): Any? {
        return settings[name]
    }

    fun getInt(name: String): Int? {
        return settings[name] as? Int
    }

    fun getDouble(name: String): Double? {
        return settings[name] as? Double
    }

    fun getBoolean(name: String): Boolean? {
        return settings[name] as? Boolean
    }

    fun getString(name: String): String? {
        return settings[name] as? String
    }

    fun setSetting(name: String, value: Any) {
        settings[name] = value
    }

    fun setInt(name: String, value: Int) {
        settings[name] = value
    }

    fun setDouble(name: String, value: Double) {
        settings[name] = value
    }

    fun setBoolean(name: String, value: Boolean) {
        settings[name] = value
    }

    fun setString(name: String, value: String) {
        settings[name] = value
    }

    fun save(): String {
        val builder = StringBuilder()
        for ((name, value) in settings) {
            builder.append("$name : ${value.javaClass.simpleName} : $value\n")
        }
        return builder.toString()
    }
}