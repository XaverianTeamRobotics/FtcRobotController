package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constants.*;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constraints.MaxVelocityTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.dw.EncoderForwardOffsetExperimentalTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.dw.EncoderTrackWidthExperimentalTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.ff.*;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.following.FollowerTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.following.SplineTest;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoader;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoaderFailureException;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class OdometryTuner extends OperationMode implements TeleOperation {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;

    public enum InitialTesting {
        BEFORE,
        TEST,
        RETUNE,
        COMPLETE
    }

    public enum EndOfTuning {
        SAVE,
        Q1,
        Q2
    }

    @Override
    public void construct() {
        if(State.firstTime) {
            State.firstTime = false;
        }else{
            RobotLog.clearGlobalWarningMsg();
            RobotLog.addGlobalWarningMessage("The tuning OpMode has been executed before, but the robot must restart between runs of this OpMode. Restarting bot...");
            Clock.sleep(10000);
            RobotLog.clearGlobalWarningMsg();
            RobotLog.addGlobalWarningMessage("If the robot doesn't restart within a minute, do it manually in real life, i.e. flipping the power switch off for a few seconds.");
            Clock.sleep(5000);
            reboot();
        }
        registerFeature(new Directions());
        registerFeature(new MotorConfigSetup());
        registerFeature(new RPMTuner());
        registerFeature(new TickTuner());
        registerFeature(new WheelRadiusTuner());
        registerFeature(new GearRatioTuner());
        registerFeature(new TrackWidthEstimateTuner());
        registerFeature(new BaseConstraintEstimateTuner());
        registerFeature(new EncoderTickTuner());
        registerFeature(new EncoderWheelRadiusTuner());
        registerFeature(new EncoderGearRatioTuner());
        registerFeature(new EncoderTrackWidthEstimateTuner());
        registerFeature(new EncoderForwardOffsetEstimateTuner());
        registerFeature(new MaxVelocityTuner());
        registerFeature(new EncoderTrackWidthExperimentalTuner());
        registerFeature(new EncoderForwardOffsetExperimentalTuner());
        registerFeature(new AutoFeedforwardTuner());
        registerFeature(new ManualFeedforwardTuner());
        registerFeature(new LateralMultiplierTuner());
        registerFeature(new TrackWidthTuner());
        registerFeature(new ManualTrackWidthTuner());
        registerFeature(new FollowerTuner());
        registerFeature(new SplineTest());
    }

    @Override
    public void run() {
        if(State.beginningDirections == Affair.PRESENT) {
            // first we go through all the constants
            AsyncQuestionExecutor.askC1(new Menu.MenuBuilder().setDescription("This OpMode will go through a series of tuning stages to set up your odometry system. First, navigate to http://192.168.43.1:8080/dash and open the OdometrySettings dropdown. You'll need to edit those values throughout the tuning process. The values you set there will be saved at the end of the tuning process. Place your bot on an empty square of field tiles. Make sure you're using a freshly-charged battery. Connect two controllers to the Driver Station. All interactions with the menu will be made with the first gamepad, and all interactions with the robot will be made with the second gamepad. When you're ready, select Start Tuning.").addItem("Start Tuning").build(), a -> {
                State.beginningDirections = Affair.PAST;
                State.constantDirections = Affair.PRESENT;
            });
        }else if(State.beginPhysicalTuning == Affair.PRESENT) {
            switch(State.initialTesting) {
                case BEFORE:
                    // now we need to start doing physical tuning, but first we need to test the constant motor and encoder values are right
                    AsyncQuestionExecutor.askC1(new Menu.MenuBuilder().setDescription("You're done defining the initial values! Soon we will begin physically tuning the robot. Before that though, we're going to do a test to make sure your values are correct. When you're ready, select Ok.").addItem("Ok").build(), a -> {
                        State.initialTesting = InitialTesting.TEST;
                    });
                    break;
                case TEST:
                    // so, we let the user run the robot around a bit and if they think something needs to be retuned, they can retune it
                    // we first init the fancy async menu (needs to be async so they can control the bot at the same time as the menu)
                    if(menuManager == null) {
                        menuManager = Questions.askAsync(Devices.controller1, "You should be able to drive your robot around with the second gamepad. Drive the bot and check the dashboard's field viewer. If the bot is driving correctly (in real life) and the \"bot\" drawn on the dashboard seems to roughly line up with the physical bot, select Continue. Otherwise, select Reconfigure and change the names/directions of your motors and encoders.", "Continue", "Reconfigure");
                    }
                    menuManager.runOnce();
                    // then the drivetrain
                    if(driver == null) {
                        driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                        driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    // and they can drive it
                    driver.setWeightedDrivePower(
                        new Pose2d(
                            -Compressor.compress(Devices.controller2.getLeftStickY()),
                            -Compressor.compress(Devices.controller2.getLeftStickX()),
                            -Compressor.compress(Devices.controller2.getRightStickX())
                        )
                    );
                    driver.update();
                    // and lastly they can choose what to do next, while they're driving!
                    Item answer = menuManager.runOnce();
                    if(answer != null) {
                        if(answer.equals("Continue")) {
                            State.initialTesting = InitialTesting.COMPLETE;
                        }else{
                            State.initialTesting = InitialTesting.RETUNE;
                        }
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                    break;
                case RETUNE:
                    // we dont need to do as much stuff here, just let the user do their things
                    AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Update your drive and encoder motor names and directions in the settings, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
                        State.initialTesting = InitialTesting.TEST;
                    });
                    break;
                case COMPLETE:
                    // now lets start the actual physical tuning
                    AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Ok, now that you've tested your initial values we can begin physical tuning! Select Start Tuning when you're ready.").addItem("Start Tuning").build(), Devices.controller1, a -> {
                        State.beginPhysicalTuning = Affair.PAST;
                        State.maxVelocityTuner = Affair.PRESENT;
                    });
                    break;
            }
        }else if(State.endTuning == Affair.PRESENT) {
            switch(State.endOfTuning) {
                case SAVE:
                    boolean saved = true;
                    try {
                        SettingLoader.save();
                    } catch(SettingLoaderFailureException e) {
                        System.out.println("Saving settings failed! " + e.getMessage());
                        e.printStackTrace();
                        saved = false;
                    }
                    if(saved) {
                        State.endOfTuning = EndOfTuning.Q1;
                    }else{
                        State.endOfTuning = EndOfTuning.Q2;
                    }
                    break;
                // we're done!!1
                case Q1:
                    AsyncQuestionExecutor.askC1("\uD83C\uDF89 You're done! Your odometry settings have been saved and will persist between OpModes and restarts.", new String[] {"Exit"}, a -> {
                        requestOpModeStop();
                    });
                    break;
                case Q2:
                    AsyncQuestionExecutor.askC1("\uD83C\uDF89 You're done! However, your odometry settings did NOT save. You need to manually edit the settings yourself in the source code and then delete the file sdcard/odo7/settings.odo7. Connect to ADB to access the filesystem and check logcat for more details. Select Exit when you're done.", new String[] {"Exit"}, a -> {
                        requestOpModeStop();
                });
            }
        }
    }

}
