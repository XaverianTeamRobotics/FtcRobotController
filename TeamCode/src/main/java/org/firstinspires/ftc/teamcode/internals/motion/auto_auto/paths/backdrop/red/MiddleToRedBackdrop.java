package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.red;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class MiddleToRedBackdrop extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineTo(new Vector2d(37.07, -23.49), Math.toRadians(294.54))
                .splineTo(getEndPosition(), Math.toRadians(0));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(0, -12);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(48.00, -36.00);
    }
}
