package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;

import java.util.ArrayList;
import java.util.Random;

public class BestPathFinder {
    private ArrayList<ArrayList<AutoAutoPathSegment>> allPaths;
    private ArrayList<AutoAutoPathSegment> pathSegments;

    public ArrayList<ArrayList<AutoAutoPathSegment>> getAllPaths() {
        ArrayList<ArrayList<AutoAutoPathSegment>> allPaths = new ArrayList<>();

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
        this.allPaths = allPaths;
        return allPaths;
    }
}