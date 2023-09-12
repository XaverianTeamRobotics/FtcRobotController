package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.dw;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.hardware.DcMotor;
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

public class EncoderTrackWidthExperimentalTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;

    private double headingAccumulator = 0;
    private double lastHeading = 0;

    private double width = 0;
    private boolean retuning = false;

    /**
     * Very important. Defines the number of turns used to calculate track width.
     */
    private int turns = 10;

    private enum Step {
        DRIVE,
        DRAW,
        SPIN,
        SHOW,
        RETUNE,
        FINISH,
        RECONF_CONST,
        NEXT
    }

    private Step step = Step.DRIVE;

    @Override
    public boolean when() {
        return State.encoderTrackWidthExperimentalTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case DRIVE:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to tune the encoder track width next. Use the second gamepad to drive your bot to a suitable position to make a 360 degree turn, then select Ok.", "Ok");
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
                        step = Step.DRAW;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case DRAW:
                AsyncQuestionExecutor.ask(Devices.controller1, "Now, make a mark on your bot and make a similar mark at the same place on your field. These marks should line up. You can also use any other method of lining the robot up with a theoretical line, you just need to keep track of the robot's current heading somehow. Once you're done, select Ok.", new String[] {"Ok"}, a -> {
                    step = Step.SPIN;
                });
                break;
            case SPIN:
                // instructions
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "Now, slowly spin your bot counterclockwise " + turns + " times using the right joystick on the second controller. Watch to see if the bot drawn on the Dashboard field follows the heading of the physical bot. MAKE SURE TO LINE YOUR ROBOT'S HEADING UP WITH THE MARKING YOU JUST MADE ON THE FIELD WHEN YOU'RE DONE (it's extremely important), and then select Ok. If the bot oscilates around a point on the Dashboard, that should be okay because your center of rotation hasn't been tuned yet. It will not affect localization precision for now.", "Ok");
                }
                menuManager.runOnce();
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                }
                // spinning logic from RR's quickstart tracking wheel lat. dist. tuner
                Pose2d vel = new Pose2d(0, 0, -Compressor.compress(Devices.controller2.getRightStickX()));
                driver.setDrivePower(vel);
                driver.update();
                double heading = driver.getPoseEstimate().getHeading();
                double deltaHeading = heading - lastHeading;
                headingAccumulator += Angle.normDelta(deltaHeading);
                lastHeading = heading;
                Item answer1 = menuManager.runOnce();
                // once we're done, we move on to the next step
                if(answer1 != null && answer1.equals("Ok")) {
                    width = (headingAccumulator / (turns * Math.PI * 2)) * PodLocalizer.LATERAL_DISTANCE;
                    menuManager = null;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    step = Step.SHOW;
                }
                break;
            case SHOW:
                if(!retuning) {
                    AsyncQuestionExecutor.ask(Devices.controller1, "Your robot seemed to turn " + headingAccumulator + " degrees. Your encoder track width should be " + width + " inches. If this seems incorrect, select Reconfigure. Otherwise, set your ENCODER_TRACK_WIDTH to the value displayed and select Continue.", new String[] {"Continue", "Reconfigure"}, a -> {
                        if(a.equals("Continue")) {
                            step = Step.FINISH;
                        }else{
                            step = Step.RETUNE;
                        }
                        headingAccumulator = 0;
                        lastHeading = 0;
                        width = 0;
                    });
                }else{
                    // allow for retuning, it might be necessary
                    AsyncQuestionExecutor.ask(Devices.controller1, "Your robot seemed to turn " + headingAccumulator + " degrees. Did the Dashboard bot match your physical bot? If so, select Continue. Otherwise, select Reconfigure.", new String[] {"Continue", "Reconfigure"}, a -> {
                        if(a.equals("Continue")) {
                            step = Step.FINISH;
                        }else{
                            step = Step.RETUNE;
                        }
                        headingAccumulator = 0;
                        lastHeading = 0;
                        width = 0;
                    });
                }
                break;
            case RETUNE:
                AsyncQuestionExecutor.askC1("Manually set your ENCODER_TRACK_WIDTH to what you think it should be. You'll run the same tuning process again, but this time pay careful attention to the bot rendered on the Dashboard. Make sure its heading matches your robot's heading. If it turns slower than the physical bot, decrease the ENCODER_TRACK_WIDTH the next time you retune. If it turns faster, increase the ENCODER_TRACK_WIDTH the next time you retune. Keep doing this until the headings match. Select Ok when you're ready.", new String[] {"Ok"}, a -> {
                    // reset and loop back to tuning
                    retuning = true;
                    step = Step.SPIN;
                });
                break;
            case FINISH:
                if(menuManager == null) {
                    menuManager = Questions.askAsyncC1("You've tuned your encoder's track width! Now we're going to make sure the localizer is tuned properly and bug-free. Use the second controller to drive your bot around, checking the Dashboard bot to see if it matches up with your bot. If you have any issues, visit bit.ly/deadwheel to troubleshoot and reconfigure. Select Reconfigure Constants if you need to reconfigure any settings besides ENCODER_TRACK_WIDTH. To reconfigure the track width, select Reconfigure Track Width. If everything's okay, select Continue.", "Continue", "Reconfigure Constants", "Reconfigure Track Width");
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
                    }else if(it.equals("Reconfigure Constants")) {
                        step = Step.RECONF_CONST;
                    }else{
                        step = Step.RETUNE;
                    }
                    menuManager = null;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                }
                break;
            case RECONF_CONST:
                AsyncQuestionExecutor.askC1("Reconfigure your odometry settings now. When you're done, select Ok. You will be able to test your robot again to see if your edits were successful.", new String[] {"Ok"}, a -> {
                    // this should be false so they can go through the automatic width tuner again -- if they changed something and need to redo their track width, the robot should try to do it itself before handing it off to the user
                    retuning = false;
                });
                break;
            case NEXT:
                State.autoFeedforwardTuner = Affair.PRESENT;
                State.encoderTrackWidthExperimentalTuner = Affair.PAST;
                break;
        }
    }

}
