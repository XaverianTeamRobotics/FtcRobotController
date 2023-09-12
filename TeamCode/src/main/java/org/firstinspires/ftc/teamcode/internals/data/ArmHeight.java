package org.firstinspires.ftc.teamcode.internals.data;

public class ArmHeight {

    private static double height[] = null;

    public static void setHeight(double[] height) {
        ArmHeight.height = height;
    }

    public static double[] getHeight() {
        return height;
    }
}
