package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.MathFunction;
import org.firstinspires.ftc.teamcode.internals.math.geometry.Line;

import java.util.ArrayList;

@Config
public class AutoNoNavigationZones {
    public static ArrayList<NoNavZone> NO_NAV_ZONES = new ArrayList<>();

    /**
     * The minimum distance between the center of the robot and the nearest no navigation zone
     * (Should be greater than the robot's largest dimension)
     */
    public static int ROBOT_MIN_SAFE_RADIUS = 12;

    public static class NoNavZone {
        private Vector2d point1;
        private Vector2d point2;

        public NoNavZone(Vector2d point1, Vector2d point2) {
            this.point1 = point1;
            this.point2 = point2;

            // Make sure point1 is the bottom left point and point2 is the top right point
            if (point1.getX() > point2.getX()) {
                double temp = point1.getX();
                point1 = new Vector2d(point2.getX(), point1.getY());
                point2 = new Vector2d(temp, point2.getY());
            }

            if (point1.getY() > point2.getY()) {
                double temp = point1.getY();
                point1 = new Vector2d(point1.getX(), point2.getY());
                point2 = new Vector2d(point2.getX(), temp);
            }
        }
    }

    public static void addNoNavZone(Vector2d point1, Vector2d point2) {
        NO_NAV_ZONES.add(new NoNavZone(point1, point2));
    }

    public static void addCenterstageDefaults() {
        NO_NAV_ZONES.add(new NoNavZone(new Vector2d(24, -12), new Vector2d(72, 12)));
    }

    private static boolean isIntersecting(ArrayList<Vector2d> points, boolean checkRadius) {
        // Check if any of the points are inside any of the no navigation zones
        for (Vector2d point : points)
            for (NoNavZone zone : NO_NAV_ZONES)
                if (isInside(point.getX(), point.getY(), zone.point1, zone.point2))
                    return true;

        if (checkRadius) { // We need to check the radius around each point, defined by ROBOT_MIN_SAFE_RADIUS
            // Generate an array of points along the circumference of the circle around each point
            // with a step size of 1 degree
            ArrayList<Vector2d> pointsAround = new ArrayList<>();
            for (Vector2d point : points) {
                for (double theta = 0; theta < 360; theta += 1) {
                    double x = point.getX() + ROBOT_MIN_SAFE_RADIUS * Math.cos(Math.toRadians(theta));
                    double y = point.getY() + ROBOT_MIN_SAFE_RADIUS * Math.sin(Math.toRadians(theta));
                    pointsAround.add(new Vector2d(x, y));
                }
            }

            if (isIntersecting(pointsAround, false)) return true;
        }
        return false;
    }

    public static boolean isIntersecting(Line line, boolean checkRadius) {
        // Generate an array of points along the line
        double m = (line.getPoint2().getY() - line.getPoint1().getY()) / (line.getPoint2().getX() - line.getPoint1().getX());

        MathFunction f = x -> line.getPoint1().getY() + m * (x - line.getPoint1().getX());

        ArrayList<Vector2d> points = new ArrayList<>();
        // If the line is vertical, we need to use a different method
        if (!Double.isInfinite(m)) {
            for (double x = line.getPoint1().getX(); x <= line.getPoint2().getX(); x += 0.01) {
                points.add(new Vector2d(x, f.evaluate(x)));
            }
        } else {
            // Get a y-value starting from point1, incrementing by 0.01 until we reach point2
            for (double y = line.getPoint1().getY(); y <= line.getPoint2().getY(); y += 0.01) {
                points.add(new Vector2d(line.getPoint1().getX(), y));
            }
        }

        return isIntersecting(points, checkRadius);
    }

    public static boolean isIntersecting(Line line) {
        return isIntersecting(line, true);
    }

    private static boolean isInside(double x, double y, Vector2d point1, Vector2d point2) {
        return (point1.getX() <= x && x <= point2.getX()) && (point1.getY() <= y && y <= point2.getY());
    }
}
