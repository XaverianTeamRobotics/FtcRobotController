package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

/**
 * <blockquote>
 *     No. We are not naming them the I and J cameras. No one will understand.<br>
 *     <cite>&ndash; Braden</cite>
 * </blockquote>
 */
public class JCam extends Feature implements Buildable {

    private static boolean down = false;

    private static long time;

    @Override
    public void build() {
        Devices.servo2.setPosition(100);
        time = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if(down) {
            Devices.servo2.setPosition(0);
        }else{
            Devices.servo2.setPosition(100);
        }
    }

    public static void toggle() {
        down = !down;
        time = System.currentTimeMillis() + 1000;
    }

    public static boolean complete() {
        return System.currentTimeMillis() > time;
    }

    public static boolean down() {
        return down;
    }

}
