package org.firstinspires.ftc.teamcode.internals.settings;

/**
 * Exception thrown when saving or loading {@link OdometrySettingsDashboardConfiguration} from a file fails for whatever reason.
 */
public class SettingLoaderFailureException extends RuntimeException {

    public SettingLoaderFailureException() {
        super();
    }

    public SettingLoaderFailureException(String message) {
        super(message);
    }

    public SettingLoaderFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
