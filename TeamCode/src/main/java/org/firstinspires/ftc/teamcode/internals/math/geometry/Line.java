package org.firstinspires.ftc.teamcode.internals.math.geometry;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.MathFunction;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public class Line {
    protected Vector2d point1;
    protected Vector2d point2;
    private double slope;

    public Line(Vector2d point1, Vector2d point2) {
        this.point1 = point1;
        this.point2 = point2;
        slope = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
    }

    public double getLength() {
        return point1.distTo(point2);
    }

    public MathFunction getFunction() {
        return x -> point1.getY() + slope * (x - point1.getX());
    }

    public double getSlope() {
        return slope;
    }

    public TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder) {
        return builder.lineTo(point1).lineTo(point2);
    }

    public Vector2d getPoint1() {
        return point1;
    }

    public Vector2d getPoint2() {
        return point2;
    }
}