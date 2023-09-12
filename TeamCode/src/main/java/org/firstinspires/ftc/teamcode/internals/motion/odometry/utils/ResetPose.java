package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class ResetPose extends OperationMode implements AutonomousOperation {

    @Override
    public void construct() {
        PoseBucket.forceSetPose(new Pose2d(0, 0, 0));
    }

    @Override
    public void run() {
        requestOpModeStop();
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

}
