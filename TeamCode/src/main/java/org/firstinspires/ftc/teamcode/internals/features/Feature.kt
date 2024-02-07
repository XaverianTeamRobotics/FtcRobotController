package org.firstinspires.ftc.teamcode.internals.features;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptTemplate;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

/**
 * A feature is a type of jlooping script. Like any script, features can be added to a runner at any time and will execute normally in the runner. According to their name, features are designed to be specific features&mdash;not an entire replacement for an {@link OperationMode}. When features are in use, OpModes should add them to the runner in {@link OperationMode#construct()} via {@link OperationMode#registerFeature(Feature)} and the features should take care of operation of their specific tasks. Features can implement the {@link Buildable} interface to mimick {@link OperationMode#construct()}, and {@link Conditional} to only execute when their {@link Conditional#when()} method returns true. State machines are encouraged to be created via conditional features rather than a switch statement or if/else chain.
 */
public abstract class Feature extends ScriptTemplate {

    public ScriptParameters environment;

    public Feature() {
        super("", true);
        // override needsinit to follow reflection patterns of opmodes
        // if opmodes use reflection, why shouldnt features?
        this.needsInit = Buildable.class.isAssignableFrom(this.getClass());
        // also its super useful to just use the class name as the script name, have a ssot just like opmodes
        this.name = this.getClass().getName();
    }

    @Override
    public void init(ScriptParameters parameters) {
        Buildable feature = (Buildable) this;
        environment = parameters;
        feature.build();
        this.needsInit = false;
    }

    @Override
    public void run(ScriptParameters parameters) {
        environment = parameters;
        if(Conditional.class.isAssignableFrom(this.getClass())) {
            Conditional feature = (Conditional) this;
            if(feature.when()) {
                loop();
            }
        }else{
            loop();
        }
    }

    /**
     * The method to run on every loop of this feature. If this feature is conditional, this will only run if {@link Conditional#when()} returns true.
     */
    public abstract void loop();

}
