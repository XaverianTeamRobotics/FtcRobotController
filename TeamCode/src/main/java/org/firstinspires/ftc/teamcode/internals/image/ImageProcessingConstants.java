package org.firstinspires.ftc.teamcode.internals.image;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ImageProcessingConstants {
    // The old values for the R + G + B cone
    public static double RED_H_MIN = 0;
    public static double RED_S_MIN = 53;
    public static double RED_V_MIN = 73;

    public static double RED_H_MAX = 20;
    public static double RED_S_MAX = 255;
    public static double RED_V_MAX = 255;

    public static double BLUE_H_MIN = 100;
    public static double BLUE_S_MIN = 53;
    public static double BLUE_V_MIN = 73;

    public static double BLUE_H_MAX = 120;
    public static double BLUE_S_MAX = 255;
    public static double BLUE_V_MAX = 255;

    public static double GREEN_H_MIN = 40;
    public static double GREEN_S_MIN = 53;
    public static double GREEN_V_MIN = 73;

    public static double GREEN_H_MAX = 80;
    public static double GREEN_S_MAX = 255;
    public static double GREEN_V_MAX = 255;


    // The new values for the Magenta + Orange + Green Cone
    public static double MAGENTA_H_MIN = 160;
    public static double MAGENTA_S_MIN = 53;
    public static double MAGENTA_V_MIN = 73;

    public static double MAGENTA_H_MAX = 255;
    public static double MAGENTA_S_MAX = 255;
    public static double MAGENTA_V_MAX = 255;


    public static double ORANGE_H_MIN = 10;
    public static double ORANGE_S_MIN = 53;
    public static double ORANGE_V_MIN = 73;

    public static double ORANGE_H_MAX = 45;
    public static double ORANGE_S_MAX = 255;
    public static double ORANGE_V_MAX = 255;
    

    public static double GREEN2_H_MIN = 40;
    public static double GREEN2_S_MIN = 53;
    public static double GREEN2_V_MIN = 73;

    public static double GREEN2_H_MAX = 80;
    public static double GREEN2_S_MAX = 255;
    public static double GREEN2_V_MAX = 255;

    public static double CANNY_1 = 100;
    public static double CANNY_2 = 200;

    public static double GAUSSIAN_BLUR_SIZE = 15;

    public static double BLACK_R_MIN = 0;
    public static double BLACK_G_MIN = 0;
    public static double BLACK_B_MIN = 0;

    public static double BLACK_R_MAX = 30;
    public static double BLACK_G_MAX = 30;
    public static double BLACK_B_MAX = 30;
}
