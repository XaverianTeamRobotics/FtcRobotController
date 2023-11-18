package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

public interface AutoAutoPathSegment {
    /**
     * Adds the path segment to the builder
     * @param builder the builder to add the path segment to
     * @return the builder with the path segment added
     */
    TrajectorySequenceBuilder addPathSegment(TrajectorySequenceBuilder builder);

    /**
     * Gets the start position of the path segment
     * @return the start position of the path segment
     */
    Vector2d getStartPosition();

    /**
     * Gets the end position of the path segment
     * @return the end position of the path segment
     */
    Vector2d getEndPosition();

    /**
     * Gets the start rotation of the path segment
     * @return the start rotation of the path segment
     */
    double getStartRotation();

    /**
     * Gets the end rotation of the path segment
     * @return the end rotation of the path segment
     */
    double getEndRotation();
}
