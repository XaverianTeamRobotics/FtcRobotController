package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.dw;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.MovingStatistics;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.PodLocalizer;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class EncoderForwardOffsetExperimentalTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;

    private double angle = 180;
    private int trials = 5;
    private int delay = 1000;

    private enum Step {
        BEGIN,
        TEST,
        VIEW,
        CONFIRM,
        RETUNE_CONST,
        NEXT
    }

    private Step step = Step.BEGIN;

    private String offset;

    @Override
    public boolean when() {
        return State.encoderForwardOffsetExperimentalTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case BEGIN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "Now we're going to tune the middle encoder's forward offset. Use the second gamepad to drive your bot to a suitable position to make a 360 degree turn, then select Ok. The bot will automatically rotate 900 degrees and return the offset of your middle encoder.", "Ok");
                }
                menuManager.runOnce();
                // we let them drive to the right spot
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                driver.setWeightedDrivePower(
                    new Pose2d(
                        -Compressor.compress(Devices.controller2.getLeftStickY()),
                        -Compressor.compress(Devices.controller2.getLeftStickX()),
                        -Compressor.compress(Devices.controller2.getRightStickX())
                    )
                );
                driver.update();
                Item answer = menuManager.runOnce();
                // then we stop if the user's done
                if(answer != null) {
                    if(answer.equals("Ok")) {
                        step = Step.TEST;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case TEST:
                DSLogging.log("Tuning offset...");
                DSLogging.update();
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                MovingStatistics forwardOffsetStats = new MovingStatistics(trials);
                for(int i = 0; i < trials; i++) {
                    driver.setPoseEstimate(new Pose2d());
                    // it is important to handle heading wraparounds
                    double headingAccumulator = 0;
                    double lastHeading = 0;
                    driver.turnAsync(Math.toRadians(angle));
                    while(!HardwareGetter.getOpMode().isStopRequested() && driver.isBusy()) {
                        double heading = driver.getPoseEstimate().getHeading();
                        headingAccumulator += Angle.norm(heading - lastHeading);
                        lastHeading = heading;
                        driver.update();
                    }
                    double forwardOffset = PodLocalizer.FORWARD_OFFSET +
                        driver.getPoseEstimate().getY() / headingAccumulator;
                    forwardOffsetStats.add(forwardOffset);
                    Clock.sleep(delay);
                }
                offset = Misc.formatInvariant("Your effective forward offset was calculated to be %.2f (SE = %.3f).",
                    forwardOffsetStats.getMean(),
                    forwardOffsetStats.getStandardDeviation() / Math.sqrt(trials));
                // we should clean up things at the end, just to make sure everythings safe
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                DSLogging.clear();
                DSLogging.update();
                step = Step.VIEW;
                break;
            case VIEW:
                AsyncQuestionExecutor.askC1(offset + " Set your ENCODER_FORWARD_OFFSET to this value, and then select Continue.", new String[] {"Continue"}, a -> {
                    step = Step.CONFIRM;
                });
                break;
            case CONFIRM:
                if(menuManager == null) {
                    menuManager = Questions.askAsyncC1("You've tuned your encoder's track width! Now we're going to make sure the localizer is tuned properly and bug-free again. Use the second controller to drive your bot around, checking the Dashboard bot to see if it matches up with your bot. If you have any issues, visit bit.ly/deadwheel to troubleshoot and reconfigure. Select Reconfigure if you need to reconfigure any settings. If everything's okay, select Continue.", "Continue", "Reconfigure");
                }
                // let them test it out
                menuManager.runOnce();
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                driver.setWeightedDrivePower(
                    new Pose2d(
                        -Compressor.compress(Devices.controller2.getLeftStickY()),
                        -Compressor.compress(Devices.controller2.getLeftStickX()),
                        -Compressor.compress(Devices.controller2.getRightStickX())
                    )
                );
                driver.update();
                Item it = menuManager.runOnce();
                // and let them reconfigure what they need (if they need to at all)
                if(it != null) {
                    if(it.equals("Continue")) {
                        step = Step.NEXT;
                    }else{
                        step = Step.RETUNE_CONST;
                    }
                    menuManager = null;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                }
                break;
            case RETUNE_CONST:
                AsyncQuestionExecutor.askC1("Reconfigure your odometry settings now. When you're done, select Ok. The offset will be retuned and you will be able to test your robot again to see if your edits were successful.", new String[] {"Ok"}, a -> {
                    step = Step.TEST;
                });
                break;
            case NEXT:
                State.lateralMultiplierTuning = Affair.PRESENT;
                State.encoderForwardOffsetExperimentalTuner = Affair.PAST;
                break;
        }

    }

}
