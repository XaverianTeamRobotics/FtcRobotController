package org.firstinspires.ftc.teamcode.internals.settings;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Settings for an odometry implementation using three odometry pods or two odometry pods and an intertial measurement unit. Tuning this is incredibly important for SLAM applications like autonomous driving or field-centric driving. Errors in this implementation compound continously, so it's important to make sure these values are as precise as possible.
 * <br><br>
 * This document makes numerous references to the Strafer v5 chassis by goBILDA. Its SKU is 3209-0001-0005.
 * <br>
 * DONT MAKE THIS INTO KOTLIN!!!!!
 */
public class OdometrySettings {
    /**
     * The pipeline ID for the limelight MegaTag pipeline (usually 3)
     */
    public static double LIMELIGHT_MT_PIPELINE_ID = 3;

    /**
     * The X offset for the Pinpoint (see <a href="https://www.gobilda.com/content/user_manuals/3110-0002-0001%20User%20Guide.pdf">goBILDA docs, pg 3</a>),
     * in freedom units (inches)
     */
    public static double PINPOINT_X_OFFSET = 0;

    /**
     * The Y offset for the Pinpoint (see <a href="https://www.gobilda.com/content/user_manuals/3110-0002-0001%20User%20Guide.pdf">goBILDA docs, pg 3</a>),
     * in freedom units (inches)
     */
    public static double PINPOINT_Y_OFFSET = 3.0;
}
