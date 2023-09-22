package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class EncoderWheelRadiusTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.encoderWheelRadiusTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Set the ENCODER_WHEEL_RADIUS to your dead wheels' radii in inches, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
            State.encoderWheelRadiusTuning = Affair.PAST;
            State.encoderGearRatioTuning = Affair.PRESENT;
        });
    }

}
