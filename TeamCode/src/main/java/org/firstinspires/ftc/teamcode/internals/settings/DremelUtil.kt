package org.firstinspires.ftc.teamcode.internals.settings

import DremelLoader
import android.content.Context
import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File

private val ROOT_FOLDER = AppUtil.ROOT_FOLDER
private const val DREMEL_FOLDER = "dremel"

private val CONFIG_FOLDER = File(ROOT_FOLDER, DREMEL_FOLDER)

fun DremelLoader.reload(context: Context) {
    try {
        FtcDashboard.stop(context);
        load()

        // force restarting by requiring dash instance to be null
        FtcDashboard.start(context);
    } catch (e: Exception) {
        RobotLog.e(e.toString());
        RobotLog.addGlobalWarningMessage("Settings failed to load! Check logcat for more details.");
    }
}

fun DremelLoader.reload() {
    reload(AppUtil.getDefContext());
}

fun createDremelLoader(fileName: String, clazz: Class<*>): DremelLoader {
    val l = DremelLoader(File(CONFIG_FOLDER, fileName), clazz)
    l.reload()
    return l
}