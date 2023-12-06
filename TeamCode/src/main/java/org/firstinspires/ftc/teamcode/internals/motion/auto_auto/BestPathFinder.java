package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.geometry.Line;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BlueLeftToLeftBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.OriginToBlueBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.RedRightToRightBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BestPathFinder {
    private ArrayList<ArrayList<AutoAutoPathSegment>> allPaths = new ArrayList<>();
    private ArrayList<AutoAutoPathSegment> pathSegments = new ArrayList<>();

    public BestPathFinder() {
        populatePathSegments();
        populateAllPaths();
    }

    private void populateAllPaths() {
        allPaths.clear();

        generateCombinations(new ArrayList<>(), 0);
    }

    private void generateCombinations(ArrayList<AutoAutoPathSegment> current, int start) {
        if (!current.isEmpty()) {
            allPaths.add(new ArrayList<>(current));
        }

        for (int i = start; i < pathSegments.size(); i++) {
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
    private void populatePathSegments() {
        pathSegments = new ArrayList<>();

        // ADD PATH SEGMENTS HERE!!!!
        pathSegments.add(new BlueLeftToLeftBackdrop());
        pathSegments.add(new OriginToBlueBackdrop());
        pathSegments.add(new RedRightToRightBackdrop());

        // For every two path segments, add a line which connects the start of one to the end of the other
        ArrayList<AutoAutoPathSegment> intermediatePathSegments = new ArrayList<>();
        for (AutoAutoPathSegment segment1 : pathSegments) {
            for (AutoAutoPathSegment segment2 : pathSegments) {
                if (!isVector2dEquals(segment1.getEndPosition(), segment2.getStartPosition())) {
                    if (!AutoNoNavigationZones.isIntersecting(new Line(segment2.getEndPosition(), segment1.getStartPosition()))) {
                        intermediatePathSegments.add(new LineToPathSegment(segment2.getEndPosition(), segment1.getStartPosition()));
                    }
                }
            }
        }

        pathSegments.addAll(intermediatePathSegments);
    }

    public ArrayList<ArrayList<AutoAutoPathSegment>> getAllPaths() {
        return allPaths;
    }

    public ArrayList<AutoAutoPathSegment> getPathSegments() {
        return pathSegments;
    }

    private boolean isVector2dEquals(Vector2d one, Vector2d two) {
        return one.getX() == two.getX() && one.getY() == two.getY();
    }

    public ArrayList<ArrayList<AutoAutoPathSegment>> getPathsToPoint(Vector2d start, Vector2d end) {
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
                        newPath.add(new LineToPathSegment(lastEndPosition, end));
                        result.add(newPath);
                    }
                }
            }
        }

        return result;
    }

    public TrajectorySequence getSequence(ArrayList<AutoAutoPathSegment> path, double startRotation) throws IllegalArgumentException {
        if (path.isEmpty()) throw new IllegalArgumentException("'path' cannot be empty!");
        TrajectorySequenceBuilder builder = new Auto(new Pose2d(path.get(0).getStartPosition(), startRotation)).begin();
        for (AutoAutoPathSegment segment : path) {
            builder = segment.addPathSegment(builder);
        }
        return builder.completeTrajectory();
    }

    public ArrayList<AutoAutoPathSegment> getFastestPathToPoint(Vector2d start, Vector2d end, double startRotation) {
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

        return fastestPath;
    }
}