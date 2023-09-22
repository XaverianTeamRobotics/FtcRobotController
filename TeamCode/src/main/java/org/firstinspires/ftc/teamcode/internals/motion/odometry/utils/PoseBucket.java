package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;

public class PoseBucket {

    private static Pose2d pose = new Pose2d(0, 0, 0);

    public static void setPose(Pose2d pose) {
        // this check is required because in the tuning opmodes we DONT want the pose to persist between drivers. also, if we're using SDK opmodes (hence the null check) then we're probably using rrqs tuners, which should follow the same behavior
        if(HardwareGetter.getOpMode() != null && !HardwareGetter.getOpMode().getClass().getName().contains("odometry.tuning")) {
            PoseBucket.pose = pose;
        }
    }

    public static void forceSetPose(Pose2d pose) {
        PoseBucket.pose = pose;
    }

    public static Pose2d getPose() {
        return pose;
    }

}
