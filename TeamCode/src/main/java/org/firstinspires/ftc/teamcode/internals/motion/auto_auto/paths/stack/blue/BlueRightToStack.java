package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.stack.blue;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class BlueRightToStack extends AutoAutoPathSegment {

    @Override
    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder
                .splineToSplineHeading(new Pose2d(-57.76, 35.03, Math.toRadians(-90.00)), Math.toRadians(-90.00))
                .lineTo(new Vector2d(getEndPosition().getX() - 6, getEndPosition().getY()))
                .lineToLinearHeading(new Pose2d(getEndPosition().getX(), getEndPosition().getY(), Math.toRadians(180.00)));
    }

    @Override
    public Vector2d getStartPosition() {
        return new Vector2d(START_R_X, START_L_Y);
    }

    @Override
    public Vector2d getEndPosition() {
        return new Vector2d(-52, 12);
    }

    @Override
    public int getFlags() {
        return Flags.ARM_LOWERED;
    }
}
