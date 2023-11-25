package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class OriginToBlueBackdrop implements AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineTo(new Vector2d(30.75, 9.26), Math.toRadians(36.89))
                .splineTo(getEndPosition(), Math.toRadians(0));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(0, 0);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(48.00, 36.00);
    }

    @Override
    public double getStartRotation() {
        return 0;
    }

    @Override
    public double getEndRotation() {
        return 0;
    }
}
