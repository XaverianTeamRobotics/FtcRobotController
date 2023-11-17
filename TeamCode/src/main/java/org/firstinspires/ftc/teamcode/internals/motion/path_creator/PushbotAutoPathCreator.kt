package org.firstinspires.ftc.teamcode.internals.motion.path_creator

import android.os.Environment
import android.util.Xml
import com.acmerobotics.roadrunner.geometry.Pose2d
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.PodLocalizer
import org.firstinspires.ftc.teamcode.internals.motion.path_creator.PathCreatorConfig.isBlueTeam
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging
import org.firstinspires.ftc.teamcode.internals.time.Clock
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * An OpMode that allows you to create a path for the robot to follow by pushing the robot and recording
 * the odometer values.
 *
 *
 * Exports data in XML format to the sdcard.
 */
class PushbotAutoPathCreator : OperationMode(), TeleOperation {
    private val poses = ArrayList<Pose2d>()
    private var aPushed = false
    private lateinit var drive: AutonomousDrivetrain

    override fun construct() {
        // Initialize localizer
        var angle: Double = -90.0
        if (isBlueTeam) angle *= -1
        drive = AutonomousDrivetrain(hardwareMap)
        drive.poseEstimate =
                            if (PathCreatorConfig.startOnLeft)  Pose2d(35.84375,   61.50,   Math.toRadians(angle))
                            else                                Pose2d(-35.84375,  61.50,   Math.toRadians(angle))

        drive.update()

        poses.add(drive.localizer.poseEstimate)
    }

    override fun run() {
        // Update the robot's position
        drive.update()
        // If the user presses the A button, save the current position to a list
        if(Devices.controller1.rightBumper && !aPushed) {
            aPushed = true
            poses.add(drive.localizer.poseEstimate)
        }else if(!Devices.controller1.rightBumper) {
            aPushed = false
        }
        // If the user presses the B button, then export the list of positions to a file
        if (Devices.controller1.leftBumper) {
            // Get the time and date to use as the file name
            val time = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            // Verify that the external storage is available
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                DSLogging.log("Error", "External storage not available")
                DSLogging.update()
                return
            }
            // Create a new file with the name "path_<time>.xml" on the sdcard
            val file =
                File(Environment.getExternalStorageDirectory().path + "/" + PathCreatorConfig.saveDirectory + "/path_" + time + ".xml")
            File(file.parent).mkdirs()
            file.createNewFile()
            // Generate the XML data from the list of poses and write it to the file
            val xml = Xml.newSerializer()
            xml.setOutput(file.outputStream(), "UTF-8")
            xml.startDocument("UTF-8", true)
            xml.startTag("", "poses")
            for (pose in poses) {
                xml.startTag("", "pose")
                xml.startTag("", "x")
                xml.text(pose.x.toString())
                xml.endTag("", "x")
                xml.startTag("", "y")
                xml.text(pose.y.toString())
                xml.endTag("", "y")
                xml.startTag("", "heading")
                xml.text(pose.heading.toString())
                xml.endTag("", "heading")
                xml.endTag("", "pose")
            }
            xml.endTag("", "poses")
            xml.endDocument()
            xml.flush()
            DSLogging.log("Saved at", file.absolutePath)
            DSLogging.log("Ending OpMode in 10 seconds")
            DSLogging.update()
            Clock.sleep(10000)
            requestOpModeStop()
        }
    }
}