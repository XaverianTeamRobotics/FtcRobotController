package org.firstinspires.ftc.teamcode.internals.dynamicmapping

import android.content.Context
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.RobotLog
import com.qualcomm.robotcore.util.Util
import dalvik.system.DexFile
import org.firstinspires.ftc.robotcore.internal.opmode.InstantRunHelper
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager
import org.firstinspires.ftc.teamcode.internals.base.Script
import java.io.File
import java.lang.reflect.Field
import kotlin.io.path.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * The DynamicMappingManager is responsible for managing dynamic hardware mapping for scripts.
 * By applying the UsesDynamicMapping annotation to a script, the DynamicMappingManager will
 * automatically generate a metadata file that contains information about the fields in the script
 * that are annotated with DynamicMappingField. This metadata file is then loaded by a compatible
 * configuration utility, which can generate data files, located in the data directory, that
 * contain the hardware mapping for each script.
 *
 * The RepeatableDynamicMapping annotation can be applied to a script to indicate that the script
 * can be repeated multiple times with different hardware mappings. This is useful for scripts that
 * may have more than one instance running at the same time.
 *
 * The data directory contains data files for the mapping. Each file's name does not matter, and it
 * can contain multiple entries. The entries are in INI format, with the section name being the script
 * name, with an optional "-$instanceId" suffix if the script is repeatable. If no configuration is found,
 * the default values are used.
 */
object DynamicMappingManager {
    val ROOT_DIRECTORY = Path(AppUtil.FIRST_FOLDER.absolutePath, "dynamic_mapping")
    val DATA_DIRECTORY = Path(ROOT_DIRECTORY.absolutePathString(), "data")
    val METADATA_FILE = File(ROOT_DIRECTORY.absolutePathString(), "metadata.ini")
    private val classesWithDynamicMapping = mutableSetOf<KClass<Script>>()

    fun <T: Script> applyDynamicMapping(clazz: KClass<T>, instanceId: Int = -1): T {
        val fields = getFields(clazz)
        val dynamicMappingFields = fields.filter { it.isAnnotationPresent(DynamicMappingField::class.java) }
        val data = attamptReadData(clazz, instanceId).toMutableMap()
        for (field in dynamicMappingFields) {
            if (!data.containsKey(field.name)) {
                data[field.name] = field.getAnnotation(DynamicMappingField::class.java)!!.default.toString()
            }
        }

        val constructor = clazz.constructors.firstOrNull { constructor ->
            constructor.parameters.all { parameter -> data.containsKey(parameter.name) }
        } ?: throw IllegalArgumentException("No suitable constructor found for class $clazz")

        val args = mutableMapOf<KParameter, Any>()
        constructor.parameters.forEach { parameter ->
            val value = data[parameter.name]!!
            val field = dynamicMappingFields.first { it.name == parameter.name }
            if (field.getAnnotation(DynamicMappingField::class.java)!!.hardwareType.type != parameter.type) {
                throw IllegalArgumentException("Field type does not match constructor parameter type")
            }
            val hardwareDevice = when (field.getAnnotation(DynamicMappingField::class.java)!!.hardwareType) {
                DynamicMappingHardware.MOTOR -> HardwareManager.motors[value.toInt()]
                DynamicMappingHardware.SERVO -> HardwareManager.servos[value.toInt()]
                DynamicMappingHardware.SENSOR_DISTANCE -> HardwareManager.distanceSensor[value.toInt()]
                DynamicMappingHardware.SENSOR_ENCODER -> HardwareManager.motors[value.toInt()]
                else -> throw IllegalArgumentException("Unsupported hardware type") // I understand the IDE doesn't think this is necessary, but it is for future-proofing
            }
            args[parameter] = hardwareDevice
        }

        return constructor.callBy(args)
    }

    /**
     * Due to a bug in the FTC SDK, we cannot use the @OnCreate annotation directly, but
     * instead we must have a java class call this method in its onCreate method.
     * See DynamicMappingLauncher
     */
    @JvmStatic
    fun onCreate(context: Context) {
        if (!ROOT_DIRECTORY.exists()) {
            ROOT_DIRECTORY.createDirectories()
            ROOT_DIRECTORY.createDirectory()
        }
        if (!DATA_DIRECTORY.exists()) {
            DATA_DIRECTORY.createDirectories()
            ROOT_DIRECTORY.createDirectory()
        }
        if (!METADATA_FILE.exists()) {
            METADATA_FILE.createNewFile()
        }

        RobotLog.vv("DynamicMappingManager", "Searching for classes with dynamic mapping")
        val dexFile = DexFile(AppUtil.getInstance().application.packageCodePath)
        var classNames = dexFile.entries().toList().toMutableList()
        classNames.addAll(InstantRunHelper.getAllClassNames(context));

        val packageToSearch = "org.firstinspires.ftc.teamcode"
        classNames = classNames.filter { Util.isPrefixOf(packageToSearch, it) }.toMutableList()

        val classes = classNames.mapNotNull { try { Class.forName(it) } catch (e: Exception) { null } }
        classes.forEach { clazz ->
            if (clazz.isAnnotationPresent(UsesDynamicMapping::class.java) && Script::class.java.isAssignableFrom(clazz)) {
                val casted = clazz.kotlin as KClass<Script>
                classesWithDynamicMapping.add(casted)
            }
        }
        RobotLog.vv("DynamicMappingManager", "Found ${classesWithDynamicMapping.size} class(es) with dynamic mapping")

        val metadata = classesWithDynamicMapping.map { generateMetadata(it) }
        RobotLog.vv("DynamicMappingManager", "Finished generating metadata")
        writeMetadata(metadata)
        RobotLog.vv("DynamicMappingManager", "Finished writing metadata, saved to ${METADATA_FILE.absolutePath}")
    }

    private fun generateMetadata(clazz: KClass<Script>): DynamicMappingMetadataEntry {
        if (!clazz.java.isAnnotationPresent(UsesDynamicMapping::class.java)) {
            throw IllegalArgumentException("Class must have the UsesDynamicMapping annotation")
        }
        if (!Script::class.java.isAssignableFrom(clazz.java)) {
            throw IllegalArgumentException("Class must be a subclass of Script")
        }

        val fieldDataArray = mutableListOf<DynamicMappingFieldData>()
        for (field in getFields(clazz)) {
            if (field.isAnnotationPresent(DynamicMappingField::class.java)) {
                val annotation = field.getAnnotation(DynamicMappingField::class.java)!!
                fieldDataArray.add(DynamicMappingFieldData(field.name, annotation.hardwareType, annotation.default))
            }
        }

        val repeatable = clazz.java.isAnnotationPresent(RepeatableDynamicMapping::class.java)
        return DynamicMappingMetadataEntry(clazz.simpleName!!, fieldDataArray, repeatable)
    }

    private fun writeMetadata(metadata: List<DynamicMappingMetadataEntry>) {
        val sb = StringBuilder()
        for (entry in metadata) {
            sb.append("[${entry.name}]\n")
            for (field in entry.fields) {
                sb.append("${field.name}=${field.hardwareType}\n")
                sb.append("${field.name}_default=${field.default}\n")
            }
            sb.append("repeatable=${entry.repeatable}\n\n")
        }
        METADATA_FILE.writeText(sb.toString().trim('\n'))
    }

    private fun getFields(clazz: KClass<*>): List<Field> {
        val uniqueSet: MutableSet<Field> = mutableSetOf()
        uniqueSet.addAll(clazz.java.declaredFields)
        var currentClass: Class<*>? = clazz.java.superclass
        while (currentClass != null) {
            uniqueSet.addAll(currentClass.declaredFields)
            currentClass = currentClass.superclass
        }
        return uniqueSet.toList()
    }

    private fun attamptReadData(clazz: KClass<*>, instanceId: Int): Map<String, String> {
        val dataFileList = File(DATA_DIRECTORY.absolute().pathString).listFiles()?.filter { it.extension == "ini" }
        if (dataFileList == null) {
            RobotLog.vv("DynamicMappingManager", "No data files found")
            return emptyMap()
        }
        // Parse each file to see if it contains the script entry
        for (file in dataFileList) {
            val lines = file.readLines()
            val scriptName = clazz.simpleName!!
            val scriptNameWithId = if (instanceId == -1) scriptName else "$scriptName-$instanceId"
            val scriptSection = lines.indexOfFirst { it.startsWith("[$scriptNameWithId]") }
            if (scriptSection == -1) {
                continue
            }
            val scriptData = mutableMapOf<String, String>()
            for (i in scriptSection + 1 until lines.size) {
                val line = lines[i]
                if (line.isBlank() || line.startsWith('[')) {
                    break
                }
                val split = line.split('=')
                if (split.size != 2) {
                    RobotLog.ee("DynamicMappingManager", "Invalid line in data file: $line")
                    continue
                }
                scriptData[split[0]] = split[1]
            }
            return scriptData
        }
        RobotLog.vv("DynamicMappingManager", "No data found for script $clazz${(if (instanceId == -1) "" else " with instance $instanceId")}")
        return emptyMap()
    }

    private data class DynamicMappingMetadataEntry(val name: String, val fields: List<DynamicMappingFieldData>, val repeatable: Boolean)
    private data class DynamicMappingFieldData(val name: String, val hardwareType: DynamicMappingHardware, val default: Int)
}