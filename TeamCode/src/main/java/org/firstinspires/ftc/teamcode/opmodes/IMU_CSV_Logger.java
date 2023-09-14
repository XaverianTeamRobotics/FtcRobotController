package org.firstinspires.ftc.teamcode.opmodes;

import android.os.Environment;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

import java.io.File;
import java.util.ArrayList;

public class IMU_CSV_Logger extends OperationMode implements TeleOperation {
    public ArrayList<ArrayList<Double>> history = new ArrayList<>();
    @Override
    public void construct() {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void run() {
        // Log the IMU Acceleration Data
        Logging.log("IMU aX", Devices.getImu().getAcceleration().component1());
        Logging.log("IMU aY", Devices.getImu().getAcceleration().component2());
        Logging.log("IMU aZ", Devices.getImu().getAcceleration().component3());

        // Log the IMU Gyro data
        Logging.log("IMU Azimuth", Devices.getImu().getOrientation().component1());
        Logging.log("IMU Attitude", Devices.getImu().getOrientation().component2());
        Logging.log("IMU Roll", Devices.getImu().getOrientation().component3());

        Logging.update();

        // Add the data to the history
        ArrayList<Double> data = new ArrayList<>();
        data.add(Devices.getImu().getAcceleration().component1());
        data.add(Devices.getImu().getAcceleration().component2());
        data.add(Devices.getImu().getAcceleration().component3());
        data.add(Devices.getImu().getOrientation().component1());
        data.add(Devices.getImu().getOrientation().component2());
        data.add(Devices.getImu().getOrientation().component3());
        history.add(data);

        // Save the history to a CSV file
        // Where history is an ArrayList<ArrayList<Double>>
        // The outer ArrayList is the rows, the inner ArrayList is the columns
        // The column names are: aX, aY, aZ, Azimuth, Attitude, Roll
        StringBuilder csv = new StringBuilder();
        for (ArrayList<Double> row : history) {
            for (Double column : row) {
                csv.append(column).append(",");
            }
            csv.append("\n");
        }
        // Write the CSV to a file
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/imu_data.csv");
        try {
            // If the file does not exist, create it
            if (!file.exists()) {
                file.createNewFile();
            } else {
                // If the file exists, delete it and create a new one
                file.delete();
                file.createNewFile();
            }
            java.io.FileWriter writer = new java.io.FileWriter(file);
            writer.write(csv.toString());
            writer.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
