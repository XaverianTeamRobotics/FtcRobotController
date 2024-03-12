package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths;

import android.util.Log;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.geometry.Line;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.blue.BlueLeftStartToBlueBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.red.MiddleToRedBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.red.RedRightStartToRedBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.blue.BlueRightToMiddle;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.middle.red.RedLeftToMiddle;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.blue.BlueBackdropToBlueLeftPark;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.blue.BlueBackdropToBlueRightPark;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.blue.BlueLeftToLeftBackdropPark;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.backdrop.blue.MiddleToBlueBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.red.RedBackdropToRedLeftPark;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.red.RedBackdropToRedRightPark;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.parking.red.RedRightToRightBackdropPark;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BestPathFinder {
    private static ArrayList<ArrayList<AutoAutoPathSegment>> allPaths = new ArrayList<>();
    private static ArrayList<AutoAutoPathSegment> pathSegments = new ArrayList<>();

    public static void populate() {
        populatePathSegments();
        populateAllPaths();
    }

    private static void populateAllPaths() {
        allPaths.clear();

        generateCombinations(new ArrayList<>(), 0);
    }

    private static void generateCombinations(ArrayList<AutoAutoPathSegment> current, int start) {
        if (!current.isEmpty()) {
            allPaths.add(new ArrayList<>(current));
        }

        for (int i = 0; i < pathSegments.size(); i++) {
            // Check if the end of the last segment is equal to the start of the next segment
            if (current.isEmpty() || isVector2dEquals(current.get(current.size() - 1).getEndPosition(), pathSegments.get(i).getStartPosition())) {
                ArrayList<AutoAutoPathSegment> newCurrent = new ArrayList<>(current);
                newCurrent.add(pathSegments.get(i));
                generateCombinations(newCurrent, i + 1);
            }
        }
    }

    /**
     * Populate the pathSegments list
     */
    private static void populatePathSegments() {
        pathSegments = new ArrayList<>();

        // ADD PATH SEGMENTS HERE!!!!
        pathSegments.add(new BlueRightToMiddle());
        pathSegments.add(new RedLeftToMiddle());

        pathSegments.add(new BlueLeftToLeftBackdropPark());
        pathSegments.add(new RedRightToRightBackdropPark());

        pathSegments.add(new MiddleToBlueBackdrop());
        pathSegments.add(new MiddleToRedBackdrop());

        pathSegments.add(new BlueLeftStartToBlueBackdrop());
        pathSegments.add(new RedRightStartToRedBackdrop());

        pathSegments.add(new BlueBackdropToBlueLeftPark());
        pathSegments.add(new BlueBackdropToBlueRightPark());

        pathSegments.add(new RedBackdropToRedLeftPark());
        pathSegments.add(new RedBackdropToRedRightPark());

        // TODO: To Spike Marks and from spike marks to backdrop

        // For every two path segments, add a line which connects the start of one to the end of the other
        ArrayList<AutoAutoPathSegment> intermediatePathSegments = new ArrayList<>();
        for (int i = 0; i < pathSegments.size(); i++) {
            for (int j = 0; j < pathSegments.size(); j++) {
                if (i != j) {
                    if (
                            isVector2dEquals(pathSegments.get(i).getEndPosition(), pathSegments.get(j).getStartPosition()) &&
                            AutoNoNavigationZones.isIntersecting(new Line(pathSegments.get(i).getEndPosition(), pathSegments.get(j).getStartPosition()))) {
                        intermediatePathSegments.add(new LineToPathSegment(pathSegments.get(i).getEndPosition(), pathSegments.get(j).getStartPosition()));
                    }
                }
            }
        }

        pathSegments.addAll(intermediatePathSegments);
    }

    public static ArrayList<ArrayList<AutoAutoPathSegment>> getAllPaths() {
        return allPaths;
    }

    public static ArrayList<AutoAutoPathSegment> getPathSegments() {
        return pathSegments;
    }

    private static boolean isVector2dEquals(Vector2d one, Vector2d two) {
        return one.getX() == two.getX() && one.getY() == two.getY();
    }

    public static ArrayList<ArrayList<AutoAutoPathSegment>> getPathsToPoint(Vector2d start, Vector2d end) {
        ArrayList<ArrayList<AutoAutoPathSegment>> result = new ArrayList<>();

        for (ArrayList<AutoAutoPathSegment> path : allPaths) {
            if (!path.isEmpty()) {
                Vector2d pathStart = path.get(0).getStartPosition();
                Vector2d pathEnd = path.get(path.size() - 1).getEndPosition();

                if (isVector2dEquals(pathStart, start) && isVector2dEquals(pathEnd, end)) {
                    result.add(path);
                }
            }
        }

        // If result is empty, add all paths which start with start a LineToPathSegment to the end point
        if (result.isEmpty()) {
            for (ArrayList<AutoAutoPathSegment> path : allPaths) {
                if (!path.isEmpty()) {
                    Vector2d pathStart = path.get(0).getStartPosition();

                    if (isVector2dEquals(pathStart, start)) {
                        ArrayList<AutoAutoPathSegment> newPath = new ArrayList<>(path);
                        Vector2d lastEndPosition = path.get(path.size() - 1).getEndPosition();
                        if (!AutoNoNavigationZones.isIntersecting(new Line(lastEndPosition, end))) {
                            newPath.add(new LineToPathSegment(lastEndPosition, end));
                            result.add(newPath);
                        }
                    }
                }
            }
        }

        // If the result is still empty, add all paths with a LineToPathSegment from start to the first start point
        if (result.isEmpty()) {
            for (ArrayList<AutoAutoPathSegment> path : allPaths) {
                if (!path.isEmpty()) {
                    Vector2d pathStart = path.get(0).getStartPosition();
                    Vector2d pathEnd = path.get(path.size() - 1).getStartPosition();

                    if (isVector2dEquals(pathEnd, end)) {
                        ArrayList<AutoAutoPathSegment> newPath = new ArrayList<>();
                        newPath.add(new LineToPathSegment(start, pathStart));
                        newPath.addAll(path);
                        Vector2d lastEndPosition = path.get(path.size() - 1).getEndPosition();
                        if (!AutoNoNavigationZones.isIntersecting(new Line(lastEndPosition, end))) {
                            newPath.add(new LineToPathSegment(lastEndPosition, end));
                            result.add(newPath);
                        }
                    }
                }
            }
        }

        return result;
    }

    public static TrajectorySequence getSequence(ArrayList<AutoAutoPathSegment> path, double startRotation) throws IllegalArgumentException {
        if (path.isEmpty()) throw new IllegalArgumentException("'path' cannot be empty!");
        TrajectorySequenceBuilder builder = new Auto(new Pose2d(path.get(0).getStartPosition(), startRotation)).begin();
        for (AutoAutoPathSegment segment : path) {
            try {
                builder = segment.addPathSegment(builder);
            } catch (Exception e) {
                Log.e("BestPathFinder", "Error while adding path segment " + segment.getClass().getSimpleName() + " to trajectory sequence builder", e);
            }
        }
        return builder.completeTrajectory();
    }

    public static ArrayList<AutoAutoPathSegment> getFastestPathToPoint(Vector2d start, Vector2d end, double startRotation) {
        ArrayList<ArrayList<AutoAutoPathSegment>> paths = getPathsToPoint(start, end);
        HashMap<ArrayList<AutoAutoPathSegment>, Double> sequenceDurationTable = new HashMap<>();

        for (ArrayList<AutoAutoPathSegment> path : paths) {
            TrajectorySequence sequence = getSequence(path, startRotation);
            sequenceDurationTable.put(path, sequence.duration());
        }

        // Return the sequence with the shortest time
        ArrayList<AutoAutoPathSegment> fastestPath = null;
        double shortestTime = Double.MAX_VALUE;

        for (Map.Entry<ArrayList<AutoAutoPathSegment>, Double> entry : sequenceDurationTable.entrySet()) {
            if (entry.getValue() < shortestTime) {
                shortestTime = entry.getValue();
                fastestPath = entry.getKey();
            }
        }

        if (fastestPath == null) throw new RuntimeException("No valid path was found from " + start + " to " + end);

        return fastestPath;
    }
}