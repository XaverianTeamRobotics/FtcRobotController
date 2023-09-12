package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class BaseConstraintEstimateTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.constraintsEstimateTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Using the equations provided in the settings, calculate and set your base constraints (MAX_VEL, MAX_ACCEL, MAX_ANG_VEL, MAX_ANG_ACCEL) in inches, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
            State.constraintsEstimateTuning = Affair.PAST;
            State.encoderTickTuning = Affair.PRESENT;
        });
    }

}
