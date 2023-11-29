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
        }

        public boolean isInside(double x, double y) {
            // Check if the point is inside the no navigation zone

            // Define the rectangle as 4 doubles: top, bottom, left, right

            // Top is the greater y-value
            double top = Math.max(point1.getY(), point2.getY());
            // Bottom is the lesser y-value
            double bottom = Math.min(point1.getY(), point2.getY());

            // Left is the lesser x-value
            double left = Math.min(point1.getX(), point2.getX());
            // Right is the greater x-value
            double right = Math.max(point1.getX(), point2.getX());

            return (bottom <= y && y <= top) && (left <= x && x <= right);
        }
    }

    /**
     * Adds a no navigation zone to the list of no navigation zones
     * @param point1 The bottom left point of the no navigation zone
     * @param point2 The top right point of the no navigation zone
     */
    public static void addNoNavZone(Vector2d point1, Vector2d point2) {
        NO_NAV_ZONES.add(new NoNavZone(point1, point2));
    }

    /**
     * Adds a no navigation zone to the list of no navigation zones
     * @param zone The no navigation zone to add
     */
    public static void addNoNavZone(NoNavZone zone) {
        NO_NAV_ZONES.add(zone);
    }

    /**
     * Adds the default no navigation zones for the center stage field
     */
    public static void addCenterstageDefaults() {
        addNoNavZone(new NoNavZone(new Vector2d(-24, 72), new Vector2d(0, 24)));        // Blue truss
        addNoNavZone(new NoNavZone(new Vector2d(-24, -72), new Vector2d(0, -24)));      // Red truss
        addNoNavZone(new NoNavZone(new Vector2d(60, 24), new Vector2d(72, 48)));        // Blue backdrop
        addNoNavZone(new NoNavZone(new Vector2d(60, -24), new Vector2d(72, -48)));      // Red backdrop
    }

    /**
     * Check if an array of points is intersecting any of the no navigation zones
     * @param points The array of points to check
     * @param checkRadius Whether to check the radius around each point
     * @return True if the array of points is intersecting any of the no navigation zones, false otherwise
     */
    private static boolean isIntersecting(ArrayList<Vector2d> points, boolean checkRadius) {
        // Check if any of the points are inside any of the no navigation zones
        for (Vector2d point : points)
            for (NoNavZone zone : NO_NAV_ZONES)
                if (zone.isInside(point.getX(), point.getY()))
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

            return isIntersecting(pointsAround, false);
        }
        return false;
    }

    /**
     * Check if a line is intersecting any of the no navigation zones
     * @param line The line to check
     * @param checkRadius Whether to check the radius around each point
     * @return True if the array of points is intersecting any of the no navigation zones, false otherwise
     */
    public static boolean isIntersecting(Line line, boolean checkRadius) {
        // Generate an array of points along the line
        double m = (line.getPoint2().getY() - line.getPoint1().getY()) / (line.getPoint2().getX() - line.getPoint1().getX());

        MathFunction f = line.getFunction();

        ArrayList<Vector2d> points = new ArrayList<>();
        // If the line is vertical, we need to use a different method
        if (!Double.isInfinite(m)) {
            for (double x = Math.min(line.getPoint1().getX(), line.getPoint2().getX()); x <= Math.max(line.getPoint1().getX(), line.getPoint2().getX()); x += 0.01) {
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

    /**
     * Check if a line or the radius around the line is intersecting any of the no navigation zones
     * @param line The line to check
     * @return True if the array of points is intersecting any of the no navigation zones, false otherwise
     */
    public static boolean isIntersecting(Line line) {
        return isIntersecting(line, true);
    }
}
