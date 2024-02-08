package org.firstinspires.ftc.teamcode.internals.features

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptTemplate

/**
 * A feature is a type of jlooping script. Like any script, features can be added to a runner at any time and will execute normally in the runner. According to their name, features are designed to be specific featuresnot an entire replacement for an [OperationMode]. When features are in use, OpModes should add them to the runner in [OperationMode.construct] via [OperationMode.registerFeature] and the features should take care of operation of their specific tasks. Features can implement the [Buildable] interface to mimick [OperationMode.construct], and [Conditional] to only execute when their [Conditional.when] method returns true. State machines are encouraged to be created via conditional features rather than a switch statement or if/else chain.
 */
abstract class Feature : ScriptTemplate("", true) {
    var environment: ScriptParameters? = null

    init {
        // override needsinit to follow reflection patterns of opmodes
        // if opmodes use reflection, why shouldnt features?
        this.needsInit = Buildable::class.java.isAssignableFrom(this.javaClass)
        // also its super useful to just use the class name as the script name, have a ssot just like opmodes
        this.name = this.javaClass.name
    }

    override fun init(parameters: ScriptParameters) {
        val feature = this as Buildable
        environment = parameters
        feature.build()
        this.needsInit = false
    }

    override fun run(parameters: ScriptParameters) {
        environment = parameters
        if (Conditional::class.java.isAssignableFrom(this.javaClass)) {
            val feature = this as Conditional
            if (feature.`when`()) {
                loop()
            }
        } else {
            loop()
        }
    }

    /**
     * The method to run on every loop of this feature. If this feature is conditional, this will only run if [Conditional.when] returns true.
     */
    abstract fun loop()
}
