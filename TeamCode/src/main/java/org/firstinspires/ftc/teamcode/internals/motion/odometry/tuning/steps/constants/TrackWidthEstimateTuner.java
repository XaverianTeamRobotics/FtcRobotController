package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class TrackWidthEstimateTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.trackWidthTuningEstimate == Affair.PRESENT;
    }

    @Override
    public void loop() {
        AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Set the TRACK_WIDTH to an estimate of your drive track width in inches, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
            State.trackWidthTuningEstimate = Affair.PAST;
            State.constraintsEstimateTuning = Affair.PRESENT;
        });
    }

}
