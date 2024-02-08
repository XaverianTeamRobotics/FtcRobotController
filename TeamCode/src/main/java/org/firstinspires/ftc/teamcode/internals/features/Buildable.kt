package org.firstinspires.ftc.teamcode.internals.features

/**
 * Represents a buildable feature.<br></br>
 * When this is implemented in a feature, the [.build] method will run on registration of the feature.
 */
interface Buildable {
    /**
     * The method to run when a feature is being built.
     */
    fun build()
}
