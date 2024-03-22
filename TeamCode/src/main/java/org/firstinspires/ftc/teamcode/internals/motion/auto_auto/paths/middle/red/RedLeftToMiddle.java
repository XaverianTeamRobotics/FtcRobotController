package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.red;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class RedLeftToMiddle extends AutoAutoPathSegment {
    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineToSplineHeading(new Pose2d(-57.76, -35.03, Math.toRadians(450.00)), Math.toRadians(450.00))
                .splineToSplineHeading(new Pose2d(-58.77, -14.39, Math.toRadians(450.00)), Math.toRadians(450.00))
                .splineToSplineHeading(new Pose2d(-48.26, -12.00, Math.toRadians(360.00)), Math.toRadians(360.00))
                .lineTo(getEndPosition());
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(START_R_X, -START_L_Y);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(0, -12);
    }

    @Override
    public int getFlags() {
        return Flags.ARM_LOWERED;
    }
}
