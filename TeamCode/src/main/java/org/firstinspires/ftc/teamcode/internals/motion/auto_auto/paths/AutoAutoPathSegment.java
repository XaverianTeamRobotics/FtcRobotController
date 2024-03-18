package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.jetbrains.annotations.NotNull;

import static org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.AutoAutoPathSegment.Flags.NULL_FLAG;

public abstract class AutoAutoPathSegment {
	public static final double START_L_Y = 60.86;
	public static final double START_L_X = 12.00;
	public static final double START_R_X = -36.00;
	public static final double DISTANCE_TO_SPIKE_MARK = (START_L_Y % 24) + 10;

    public static class Flags {
        public static final int NULL_FLAG = 0;
        public static final int ARM_LOWERED = 1;
        public static final int BACK_HITS_WALL = 2;
    }

	/**
     * Adds the path segment to the builder
     * @param builder the builder to add the path segment to
     * @return the builder with the path segment added
     */
    public abstract TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder);

    /**
     * Gets the start position of the path segment
     * @return the start position of the path segment
     */
    public abstract Vector2d getStartPosition();

    /**
     * Gets the end position of the path segment
     * @return the end position of the path segment
     */
    public abstract Vector2d getEndPosition();

    public int getFlags() {
        return NULL_FLAG;
    }

    public @NotNull String toString() {
        return "{AutoAutoPathSegment: Start: " + getStartPosition() + ", End: " + getEndPosition() + "}";
    }
}
