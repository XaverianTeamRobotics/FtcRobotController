package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.blue;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class BlueStackToMiddle extends AutoAutoPathSegment {

    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .back(6)
                .turn(Math.toRadians(90.00))
                .lineTo(getEndPosition());
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(-52, 12);
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
