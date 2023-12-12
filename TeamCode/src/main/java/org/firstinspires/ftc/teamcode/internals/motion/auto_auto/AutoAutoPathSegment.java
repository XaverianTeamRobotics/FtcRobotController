package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class AutoAutoPathSegment {
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

    public @NotNull String toString() {
        return "{AutoAutoPathSegment: Start: " + getStartPosition() + ", End: " + getEndPosition() + "}";
    }
}
