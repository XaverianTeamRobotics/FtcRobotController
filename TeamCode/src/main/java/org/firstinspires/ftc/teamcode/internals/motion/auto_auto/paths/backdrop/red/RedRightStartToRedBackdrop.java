package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.red;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class RedRightStartToRedBackdrop extends AutoAutoPathSegment {
	@Override
	public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
		return builder
				.splineTo(new Vector2d(48.00, -36.00), 0.0);
	}

	@Override
	public Vector2d getStartPosition() {
		return new Vector2d(START_L_X, -START_L_Y);
	}

	@Override
	public Vector2d getEndPosition() {
		return new Vector2d(48.00, -36.00);
	}
}