package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class LineToPathSegment extends AutoAutoPathSegment {
	public Vector2d startPosition, endPosition;

	public LineToPathSegment(Vector2d start, Vector2d end) {
		startPosition = start;
		endPosition = end;
	}

	@Override
	public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
		if (!(startPosition.getX() == endPosition.getX() && startPosition.getY() == endPosition.getY()))
			return builder.lineTo(endPosition);
		else return builder;
	}

	@Override
	public Vector2d getStartPosition() {
		return startPosition;
	}

	@Override
	public Vector2d getEndPosition() {
		return endPosition;
	}
}
