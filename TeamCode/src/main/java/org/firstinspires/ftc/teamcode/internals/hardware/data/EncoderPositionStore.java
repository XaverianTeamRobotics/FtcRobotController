package org.firstinspires.ftc.teamcode.internals.hardware.data;

import java.util.HashMap;

/**
 * Stores the position of encoders so they can set themselves to this position on startup. Useful for transferring encoder position between OpModes.
 */
public class EncoderPositionStore {

    private static final HashMap<String, Integer> positions = new HashMap<>();

    public static void setPosition(String name, int pos) {
        positions.put(name, pos);
    }

    public static Integer getPosition(String name) {
        if(positions.containsKey(name)) {
            Integer i = positions.get(name);
            if(i == null) {
                return 0;
            }
            return i;
        }
        return null;
    }

}
