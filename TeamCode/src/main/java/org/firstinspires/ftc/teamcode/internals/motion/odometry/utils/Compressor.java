package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

public class Compressor {

    /**
     * Compress xblib [-100, 100] and [0, 100] intervals to vanilla [-1, 1] and [0, 1] intervals.
     */
    public static double compress(double val) {
        return val / 100.0;
    }

}
