package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.red;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class RedBackdropToRedRightPark extends AutoAutoPathSegment {
	@Override
	public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
		return builder.
				lineTo(new Vector2d(48.00, -60.00))
				.lineTo(new Vector2d(60.00, -60.00));
	}

	@Override
	public Vector2d getStartPosition() {
		return new Vector2d(48.00, -36.00);
	}

	@Override
	public Vector2d getEndPosition() {
		return new Vector2d(60.00, -60.00);
	}
}
