package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.blue;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class BlueLeftToLeftBackdropPark extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder.lineTo(getEndPosition());
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(START_L_X, START_L_Y);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(60.00, 64.50);
    }
}
