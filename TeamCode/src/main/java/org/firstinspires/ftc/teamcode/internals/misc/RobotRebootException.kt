package org.firstinspires.ftc.teamcode.internals.misc;

public class RobotRebootException extends RuntimeException {

    public RobotRebootException() {
        super();
    }

    public RobotRebootException(String message) {
        super(message);
    }

    public RobotRebootException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotRebootException(Throwable cause) {
        super(cause);
    }
    
}
