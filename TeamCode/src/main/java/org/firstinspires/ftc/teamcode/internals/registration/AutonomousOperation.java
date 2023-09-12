package org.firstinspires.ftc.teamcode.internals.registration;

/**
 * Tells the {@link OperationModeRegistrar} that the {@link OperationMode} implementing this uses Auto-style control.
 */
public interface AutonomousOperation {

    /**
     * The next {@link OperationMode} to queue up to run after the completion of this Autonomous mode. The {@link OperationMode} <strong>MUST</strong> implement {@link TeleOperation}, and only {@link TeleOperation}. This will be called when this {@link OperationMode} is registered.
     * @return The next {@link OperationMode} to load
     */
    Class<? extends OperationMode> getNext();

}