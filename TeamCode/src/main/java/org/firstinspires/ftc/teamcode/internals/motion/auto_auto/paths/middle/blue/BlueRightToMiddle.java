package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.blue;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class BlueRightToMiddle extends AutoAutoPathSegment {

    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineTo(new Vector2d(-52.07, 51.02), Math.toRadians(263.37))
                .splineTo(new Vector2d(-57.38, 42.86), Math.toRadians(254.56))
                .splineTo(new Vector2d(-57.86, 31.56), Math.toRadians(270.00))
                .splineTo(new Vector2d(-49.50, 8.40), Math.toRadians(-0.88))
                .splineTo(getEndPosition(), Math.toRadians(0));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(START_R_X, START_L_Y);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(0, 12);
    }

    @Override
    public int getFlags() {
        return Flags.ARM_LOWERED;
    }
}
