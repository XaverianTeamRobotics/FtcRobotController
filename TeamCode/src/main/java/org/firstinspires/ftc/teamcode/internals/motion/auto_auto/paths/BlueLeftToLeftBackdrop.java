package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class BlueLeftToLeftBackdrop extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder.lineTo(getEndPosition());
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(12, 64.50);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(60.00, 64.50);
    }

    @Override
    public double getStartRotation() {
        return -90;
    }

    @Override
    public double getEndRotation() {
        return -90;
    }
}
