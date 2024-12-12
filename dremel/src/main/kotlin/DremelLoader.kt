import java.io.File
import java.lang.reflect.Field

/**
 * Load a Dremel file from the robot's internal storage.
 *
 * Classes which use this loader should be objects which have fields annotated with [DremelData].
 * You must also declare a `val loader = DremelLoader("file.drml", this::class.java)` in the class.
 *
 * @param fileName the name of the file to load. Should end in .drml
 * @param clazz the class to load the data into. Should be an object with fields annotated with [DremelData]
 * @see DremelInterpreter
 */
class DremelLoader(private val file: File, private val clazz: Class<*>) {
    private lateinit var interpreter: DremelInterpreter

    init {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        load()
    }

    fun load() {
        if (file.exists()) {
            val raw = file.readText()
            interpreter = DremelInterpreter(raw)
        } else {
            interpreter = DremelInterpreter("")

            val fields = clazz.declaredFields.filter { it.isAnnotationPresent(DremelData::class.java) }
            for (f in fields) {
                val name = f.name
                setFromDefault(f, name)
            }
        }

        // Check if every field has a value in the interpreter. If not, set it to the default value.
        val fields = clazz.declaredFields.filter { it.isAnnotationPresent(DremelData::class.java) }
        for (f in fields) {
            val name = f.name
            if (interpreter.getSetting(name) == null) {
                setFromDefault(f, name)
            }
        }
    }

    private fun setFromDefault(f: Field, name: String) {
        val type = f.getAnnotation(DremelData::class.java).type
        when (type) {
            DremelType.INT -> interpreter.setInt(name, f.get(null) as Int)
            DremelType.DOUBLE -> interpreter.setDouble(name, f.get(null) as Double)
            DremelType.BOOLEAN -> interpreter.setBoolean(name, f.get(null) as Boolean)
            DremelType.STRING -> interpreter.setString(name, f.get(null) as String)
        }
    }

    fun save() {
        interpreter.save()
    }

    fun getInt(name: String): Int {
        return interpreter.getInt(name)!!
    }

    fun getDouble(name: String): Double {
        return interpreter.getDouble(name)!!
    }

    fun getBoolean(name: String): Boolean {
        return interpreter.getBoolean(name)!!
    }

    fun getString(name: String): String {
        return interpreter.getString(name)!!
    }
}