package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.constraints;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

import java.util.Objects;

public class MaxVelocityTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;

    private final double VEL_RUNTIME = 2.0;
    private ElapsedTime vel_timer;
    private double velMax = 0.0;

    private final double ANG_RUNTIME = 4.0;
    private ElapsedTime ang_timer;
    private double angMax = 0.0;

    private enum Step {
        ALIGN_VEL,
        DO_VEL,
        SHOW_VEL,
        ALIGN_ANG,
        DO_ANG,
        SHOW_ANG,
        NEXT
    }

    private Step step = Step.ALIGN_VEL;

    @Override
    public boolean when() {
        return State.maxVelocityTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN_VEL:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to determine the actual maximum velocity of your robot. First, use the second controller to drive your bot to the start of a long stretch of field tiles (preferably 7+ tiles) facing forward towards the stretch, then select Ok.", "Ok");
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
                        step = Step.DO_VEL;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case DO_VEL:
                // setup everything
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    DSLogging.log("Finding maximum velocity...");
                    DSLogging.update();
                    driver.setDrivePower(new Pose2d(1, 0, 0));
                    vel_timer = new ElapsedTime();
                }
                // and now we do the actual running of the robot
                if(vel_timer.seconds() < VEL_RUNTIME) {
                    driver.updatePoseEstimate();
                    Pose2d poseVelo = Objects.requireNonNull(driver.getPoseVelocity(), "poseVelocity() must not be null. Ensure that the getWheelVelocities() method has been overridden in your localizer.");
                    velMax = Math.max(poseVelo.vec().norm(), velMax);
                }else{
                    // and lastly we just clean everything up and move on
                    driver.setDrivePower(new Pose2d());
                    driver = null;
                    DSLogging.clear();
                    DSLogging.update();
                    step = Step.SHOW_VEL;
                }
                break;
            case SHOW_VEL:
                AsyncQuestionExecutor.askC1("Your maximum velocity is " + velMax + " and that means you should set your MAX_VEL and MAX_ACCEL to no more than " + velMax * 0.75 + ". Once you've done that, select Continue.", new String[] {"Continue"}, a -> {
                    step = Step.ALIGN_ANG;
                });
                break;
            case ALIGN_ANG:
                // now, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to determine the actual angular maximum velocity of your robot. First, use the second controller to drive your bot to a suitable place to make a 360 degree turn, then select Ok.", "Ok");
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
                Item answer1 = menuManager.runOnce();
                // then we stop if the user's done
                if(answer1 != null) {
                    if(answer1.equals("Ok")) {
                        step = Step.DO_ANG;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case DO_ANG:
                // setup
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    DSLogging.log("Finding maximum angular velocity...");
                    DSLogging.update();
                    driver.setDrivePower(new Pose2d(0, 0, 1));
                    ang_timer = new ElapsedTime();
                }
                // calculation
                if(ang_timer.seconds() < ANG_RUNTIME) {
                    driver.updatePoseEstimate();
                    Pose2d poseVelo = Objects.requireNonNull(driver.getPoseVelocity(), "poseVelocity() must not be null. Ensure that the getWheelVelocities() method has been overridden in your localizer.");
                    angMax = Math.max(poseVelo.getHeading(), angMax);
                }else{
                    // and lastly one more "just clean everything up and move on"
                    driver.setDrivePower(new Pose2d());
                    driver = null;
                    DSLogging.clear();
                    DSLogging.update();
                    step = Step.SHOW_ANG;
                }
                break;
            case SHOW_ANG:
                AsyncQuestionExecutor.askC1("Your maximum angular velocity is " + angMax + " radians (" + Math.toDegrees(angMax) + "°) and that means you should set your MAX_ANG_VEL and MAX_ANG_ACCEL to no more than " + angMax * 0.75 + " radians ( " + Math.toDegrees(angMax * 0.75) + "°). Once you've done that, select Continue.", new String[] {"Continue"}, a -> {
                    step = Step.NEXT;
                });
                break;
            case NEXT:
                State.encoderTrackWidthExperimentalTuner = Affair.PRESENT;
                State.maxVelocityTuner = Affair.PAST;
                break;
        }
    }

}
