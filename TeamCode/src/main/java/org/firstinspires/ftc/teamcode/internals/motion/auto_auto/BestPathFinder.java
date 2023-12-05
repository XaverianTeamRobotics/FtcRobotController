package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BlueLeftToLeftBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.OriginToBlueBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.RedRightToRightBackdrop;

import java.util.ArrayList;
import java.util.Random;

public class BestPathFinder {
    private final ArrayList<ArrayList<AutoAutoPathSegment>> allPaths = new ArrayList<>();
    private final ArrayList<AutoAutoPathSegment> pathSegments = new ArrayList<>();

    public BestPathFinder() {
        populatePathSegments();
        populateAllPaths();
        trimAllPaths();
    }

    private void populateAllPaths() {
        allPaths.clear();

        int duplicates = 0;
        while (duplicates < 100) {
            Random rand = new Random();

            int length = rand.nextInt(pathSegments.size()) + 1;
            ArrayList<AutoAutoPathSegment> path = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                int index = rand.nextInt(pathSegments.size());
                AutoAutoPathSegment segment = pathSegments.get(index);
                if (!pathSegments.contains(segment)) path.add(segment);
                else i--;
            }

            if (allPaths.contains(path)) {
                duplicates++;
            } else {
                allPaths.add(path);
            }
        }
    }

    /**
     * Populate the pathSegments list
     */
    private void populatePathSegments() {
        pathSegments.clear();

        // ADD PATH SEGMENTS HERE!!!!
        pathSegments.add(new BlueLeftToLeftBackdrop());
        pathSegments.add(new OriginToBlueBackdrop());
        pathSegments.add(new RedRightToRightBackdrop());

        // For every two path segments, add a line which connects the start of one to the end of the other
        for (AutoAutoPathSegment segment1 : pathSegments) {
            for (AutoAutoPathSegment segment2 : pathSegments) {
                if (segment1.getStartPosition() != segment2.getEndPosition()) {
                    pathSegments.add(new LineToPathSegment(segment2.getEndPosition(), segment1.getStartPosition()));
                }
            }
        }

    }

    /**
     * Trim paths which are not continuous
     */
    private void trimAllPaths() {
        ArrayList<ArrayList<AutoAutoPathSegment>> newAllPaths = new ArrayList<>();
        for (ArrayList<AutoAutoPathSegment> path : allPaths ) {
            boolean pathValid = true;
            for (int i = 1; i < path.size(); i++) {
                if (path.get(i).getStartPosition() != path.get(i-1).getEndPosition()) {
                    pathValid = false;
                    break;
                }
            }

            if (pathValid) newAllPaths.add(path);
        }
        allPaths.clear();
        allPaths.addAll(newAllPaths);

    }

    public ArrayList<ArrayList<AutoAutoPathSegment>> getAllPaths() {
        return allPaths;
    }

    public ArrayList<AutoAutoPathSegment> getPathSegments() {
        return pathSegments;
    }
}