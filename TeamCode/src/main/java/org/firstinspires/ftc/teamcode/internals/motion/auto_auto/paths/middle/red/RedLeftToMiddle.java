package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.red;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class RedLeftToMiddle extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineTo(new Vector2d(-52.07, -51.02), Math.toRadians(96.63))
                .splineTo(new Vector2d(-57.38, -42.86), Math.toRadians(105.44))
                .splineTo(new Vector2d(-57.86, -31.56), Math.toRadians(90.0))
                .splineTo(new Vector2d(-49.50, -8.40), Math.toRadians(0.88))
                .splineTo(getEndPosition(), Math.toRadians(0));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(START_R_X, -START_L_Y);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(0, -12);
    }
}
