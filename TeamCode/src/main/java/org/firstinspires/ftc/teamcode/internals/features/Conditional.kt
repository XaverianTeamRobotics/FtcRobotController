package org.firstinspires.ftc.teamcode.internals.features;

/**
 * Represents a conditional feature. When this is implemented in a feature, this method must return true for the feature's loop method to run.
 */
public interface Conditional {
    /**
     * The method to check whether a feature should be ran or not. The return value of this method will determine whether the feature is run automatically by jlooping, you don't have to do anything.
     * @return True if the feature should run, false if it should not.
     */
    boolean when();
}
