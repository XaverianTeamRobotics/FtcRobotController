package org.firstinspires.ftc.teamcode.internals.math.geometry;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class Line {
    private Vector2d point1;
    private Vector2d point2;

    public Line(Vector2d point1, Vector2d point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Vector2d getPoint1() {
        return point1;
    }

    public Vector2d getPoint2() {
        return point2;
    }

    public double getLength() {
        return point1.distTo(point2);
    }

    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder.lineTo(point1).lineTo(point2);
    }
}