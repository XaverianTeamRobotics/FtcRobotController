package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import androidx.annotation.NonNull;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class SettingLoader {

    /**
     * The path to the settings file.
     */
    public static final String PATH = AppUtil.ROOT_FOLDER + "/odo7/settings.odo7";
    // i mean...technically we aren't using PATH for anything, but its still useful if we need it somewhere else i guess.
    private static final String PATH_INTERNAL = AppUtil.ROOT_FOLDER + "/odo7/";

    /**
     * Write to the odometry settings file.
     * @return true if the file was successfully written to, false if something went wrong
     */
    private static boolean write() {
        try {

            // we need to do this ugliness because control hub os 1.1.3 uses android 7.1.1 which uses api 25 which doesnt support java.nio :(
            File dir = new File(PATH_INTERNAL);
            dir.mkdirs();
            File file = new File(PATH_INTERNAL + "settings.odo7");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file.getPath());

            // just making a string out of the current odo settings fields
            fileWriter.write(makeString());
            fileWriter.close();
            return true;

        } catch(IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private static String makeString() {

        StringBuilder builder = new StringBuilder();
        builder.append(";");

        for(Field field : OdometrySettingsDashboardConfiguration.class.getFields()) {
            try {
                String str = makeEntry(field);
                // Entries are delimited by a semicolon
                builder.append(str).append(";");
            } catch(IllegalAccessException exception) {
                System.out.println("Failed to save field with exception '" + exception.getMessage() + "', moving on without saving this field.");
            }
        }

        // probably not necessary, but just in case
        if(!builder.toString().endsWith(";")) {
            builder.append(";");
        }

        return builder.toString();
    }

    /**
     * Entry syntax:
     * <blockquote>
     *     Field name
     *     <br>
     *     Field type
     *     <br>
     *     Field value 1
     *     <br>
     *     Field value 2
     *     <br>
     *     Field value n...
     *     <br>
     * </blockquote>
     * @throws IllegalAccessException whenever something goes wrong
     */
    private static String makeEntry(Field field) throws IllegalAccessException {

        StringBuilder str = new StringBuilder();
        str.append(System.lineSeparator()).append(field.getName()).append(System.lineSeparator());

        // reflection
        // extracts the data from fields and converts it into a string
        Class<?> type = field.getType();
        if(type == double.class) {

            try {
                str.append("Double").append(System.lineSeparator()).append(field.getDouble(null)).append(System.lineSeparator());
            } catch(NullPointerException e) {
                throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            }

        }else if(type == MotorConfig.class) {

            MotorConfig config = (MotorConfig) field.get(null);
            if(config == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            DcMotorSimple.Direction direction = config.DIRECTION;
            str.append("MotorConfig").append(System.lineSeparator()).append(config.NAME).append(System.lineSeparator());

            if(direction == DcMotorSimple.Direction.FORWARD) {
                str.append("Forward");
            }else{
                str.append("Reverse");
            }

            str.append(System.lineSeparator());

        }else if(type == EncoderConfig.class) {

            EncoderConfig config = (EncoderConfig) field.get(null);
            if(config == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            OdoEncoder.Direction direction = config.DIRECTION;
            str.append("EncoderConfig").append(System.lineSeparator()).append(config.NAME).append(System.lineSeparator());

            if(direction == OdoEncoder.Direction.FORWARD) {
                str.append("Forward");
            }else{
                str.append("Reverse");
            }

            str.append(System.lineSeparator());

        }else if(type == PIDCoefficients.class) {

            PIDCoefficients coefficients = (PIDCoefficients) field.get(null);
            if(coefficients == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");

            str.append("PIDCoefficents").append(System.lineSeparator()).append(coefficients.kP).append(System.lineSeparator()).append(coefficients.kI).append(System.lineSeparator()).append(coefficients.kD).append(System.lineSeparator());

        }else if(type == String.class) {

            String data = (String) field.get(null);
            if(data == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");

            str.append("IMU").append(System.lineSeparator()).append(data).append(System.lineSeparator());

        }
        else if(type == LocalizationType.class) {

            LocalizationType option = (LocalizationType) field.get(null);
            if(option == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");

            str.append("LocalizationType").append(System.lineSeparator());
            if(option == LocalizationType.POD) {
                str.append("pod");
            }else{
                str.append("imu");
            }
            str.append(System.lineSeparator());

        }else{
            throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed.");
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
        return str.toString();
    }

    /**
     * Read from the odometry settings file.
     * @return the settings as a string, null if something went wrong
     */
    private static String read() {
        try {

            // again, api 25 ;-;
            FileReader reader = new FileReader(PATH_INTERNAL + "settings.odo7");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                // oh. oh this second append call. it makes me LIVID.
                //                      |
                //                      V
                builder.append(line).append(System.lineSeparator());
                //  i wrote nearly 1000 lines of reflection trying to fix a bug which i assumed was a reflection bug. it was Not a reflection bug. it was a formatting bug. i thought buffered readers kept the line seperator at the end of lines. i was wrong. very wrong
                // anyway, now we have an overengineered solution with a lot more reflection than necessary but im gonna keep it because it works..and it also prevents the bug i THOUGHT we were having from happening if com.acme.dashboard switches to building configs at build time rather than runtime
            }

            return builder.toString();
        } catch(IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a map of the currently stored settings.
     */
    private static HashMap<String, Value> parseSettings() {
        String file = read();
        if(file != null) {

            // remember the reflection to convert this to text? now we do the opposite.
            HashMap<String, Value> parsed = new HashMap<>();
            String[] items = file.split(";");

            for(String item : items) {

                item = item.trim();
                String[] pieces = item.split(System.lineSeparator());
                Value value;
                ArrayList<String> trimmedPieces = new ArrayList<>();
                for(String piece : pieces) {
                    trimmedPieces.add(piece.trim());
                }

                if(trimmedPieces.size() < 2) {
                    continue;
                }

                switch(trimmedPieces.get(1)) {
                    case "Double":

                        value = new Value(Double.parseDouble(trimmedPieces.get(2)), Double.class);

                        break;
                    case "MotorConfig":

                        if(trimmedPieces.get(3).equals("Forward")) {
                            value = new Value(new MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.FORWARD), MotorConfig.class);
                        }else{
                            value = new Value(new MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.REVERSE), MotorConfig.class);
                        }

                        break;
                    case "EncoderConfig":

                        if(trimmedPieces.get(3).equals("Forward")) {
                            value = new Value(new EncoderConfig(trimmedPieces.get(2), OdoEncoder.Direction.FORWARD), EncoderConfig.class);
                        }else{
                            value = new Value(new EncoderConfig(trimmedPieces.get(2), OdoEncoder.Direction.REVERSE), EncoderConfig.class);
                        }

                        break;
                    case "PIDCoefficents":

                        value = new Value(new PIDCoefficients(Double.parseDouble(trimmedPieces.get(2)), Double.parseDouble(trimmedPieces.get(3)), Double.parseDouble(trimmedPieces.get(4))), PIDCoefficients.class);

                        break;
                    case "IMU":

                        if(trimmedPieces.get(2) != null) {
                            value = new Value(trimmedPieces.get(2), String.class);
                        }else{
                            value = new Value("", String.class);
                        }

                        break;
                    case "LocalizationType":

                        if(trimmedPieces.get(2).equals("pod")) {
                            value = new Value(LocalizationType.POD, LocalizationType.class);
                        }else{
                            value = new Value(LocalizationType.IMU, LocalizationType.class);
                        }

                        break;
                    default:
                        continue;
                }
                parsed.put(trimmedPieces.get(0), value);
            }
            return parsed;
        }else{
            throw new SettingLoaderFailureException("Settings file not found!");
        }
    }

    protected static class Value {

        public final Object obj;
        public final Class<?> clazz;

        public Value(Object object, Class<?> clazz) {
            obj = object;
            this.clazz = clazz;
        }

        @NonNull
        @NotNull
        @Override
        public String toString() {
            return obj.toString() + " " + this.clazz;
        }

    }

    /**
     * Save the current odometry settings.
     */
    public static void save() throws SettingLoaderFailureException {
        try {
            if(!write()) {
                throw new SettingLoaderFailureException("Saving settings failed from IO exception. Check logcat for more details.");
            }
        } catch(Exception e) {
            throw new SettingLoaderFailureException("Saving settings failed.", e);
        }
    }

    /**
     * Load the odometry settings from the most recent save.
     */
    protected static HashMap<String, Value> load() throws SettingLoaderFailureException {
        try {
            return parseSettings();
        } catch(Exception e) {
            throw new SettingLoaderFailureException("Loading settings failed.", e);
        }
    }

}
