package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import java.util.ArrayList;

@Config
public class AutoNoNavigationZones {
    public static ArrayList<Vector2d> NO_NAV_ZONES = new ArrayList<>();

    /**
     * The minimum distance between the center of the robot and the nearest no navigation zone
     * (Should be greater than the robot's largest dimension)
     */
    public static int ROBOT_MIN_SAFE_RADIUS = 12;

    public class NoNavZone {
        private Vector2d point1;
        private Vector2d point2;

        public NoNavZone(Vector2d point1, Vector2d point2) {
            this.point1 = point1;
            this.point2 = point2;
        }
    }
}
