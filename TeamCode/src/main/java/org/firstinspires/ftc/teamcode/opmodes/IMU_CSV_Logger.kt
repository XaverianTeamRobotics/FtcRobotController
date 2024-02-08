package org.firstinspires.ftc.teamcode.opmodes

import android.os.Environment
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.imu
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Log acceleration and gyro data from the IMU and write it to imu_data.csv.
 */
class IMU_CSV_Logger : OperationMode(), TeleOperation {
    var history: ArrayList<ArrayList<Double>> = ArrayList()
    override fun construct() {
    }

    override fun run() {
        // Log the IMU Acceleration Data
        Logging.log("IMU aX", imu.acceleration.x)
        Logging.log("IMU aY", imu.acceleration.y)
        Logging.log("IMU aZ", imu.acceleration.z)

        // Log the IMU Gyro data
        Logging.log("IMU Azimuth", imu.orientation.x)
        Logging.log("IMU Attitude", imu.orientation.y)
        Logging.log("IMU Roll", imu.orientation.z)

        Logging.update()

        // Add the data to the history
        val data = ArrayList<Double>()
        data.add(imu.acceleration.x)
        data.add(imu.acceleration.y)
        data.add(imu.acceleration.z)
        data.add(imu.orientation.x)
        data.add(imu.orientation.y)
        data.add(imu.orientation.z)
        history.add(data)

        // Save the history to a CSV file
        // Where history is an ArrayList<ArrayList<Double>>
        // The outer ArrayList is the rows, the inner ArrayList is the columns
        // The column names are: aX, aY, aZ, Azimuth, Attitude, Roll
        val csv = StringBuilder()
        for (row in history) {
            for (column in row) {
                csv.append(column).append(",")
            }
            csv.append("\n")
        }
        // Write the CSV to a file
        val file = File(Environment.getExternalStorageDirectory().path + "/imu_data.csv")
        try {
            // If the file does not exist, create it
            if (!file.exists()) {
                file.createNewFile()
            } else {
                // If the file exists, delete it and create a new one
                file.delete()
                file.createNewFile()
            }
            val writer = FileWriter(file)
            writer.write(csv.toString())
            writer.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
