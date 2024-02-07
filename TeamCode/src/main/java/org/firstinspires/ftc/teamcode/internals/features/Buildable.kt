package org.firstinspires.ftc.teamcode.internals.features;

/**
 * Represents a buildable feature.<br>
 * When this is implemented in a feature, the {@link #build()} method will run on registration of the feature.
 */
public interface Buildable {
    /**
     * The method to run when a feature is being built.
     */
    void build();
}
