package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class RPMTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.rpmTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Set the MAX_RPM to your motors' max RPM, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
            State.rpmTuning = Affair.PAST;
            State.tickTuning = Affair.PRESENT;
        });
    }

}
