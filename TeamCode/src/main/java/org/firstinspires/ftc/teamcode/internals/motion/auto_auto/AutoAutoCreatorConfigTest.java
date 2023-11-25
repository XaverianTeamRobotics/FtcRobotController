package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

public class AutoAutoCreatorConfigTest extends OperationMode implements AutonomousOperation {
    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

    @Override
    public void construct() {
        AutoAutoCreatorConfig config = new AutoAutoCreatorConfig();
        config.askQuestions();
        DSLogging.clear();
        DSLogging.log("Team color", config.getTeamColor());
        DSLogging.log("Starting position", config.getStartingPosition());
        DSLogging.log("Place backdrop", config.getPlaceBackdrop());
        DSLogging.log("Backdrop pixel position", config.getBackdropPixelPosition());
        DSLogging.log("Place spike mark", config.getPlaceSpikeMark());
        DSLogging.log("Park place", config.getParkPlace());
        DSLogging.update();
    }

    @Override
    public void run() {

    }
}