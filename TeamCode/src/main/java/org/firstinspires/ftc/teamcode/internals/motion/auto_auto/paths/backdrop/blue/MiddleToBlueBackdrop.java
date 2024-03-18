package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.blue;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class MiddleToBlueBackdrop extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineTo(new Vector2d(37.07, 23.49), Math.toRadians(65.46))
                .splineTo(getEndPosition(), Math.toRadians(0));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(0, 12);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(48.00, 36.00);
    }

    @Override
    public int getFlags() {
        return Flags.ARM_LOWERED;
    }
}
