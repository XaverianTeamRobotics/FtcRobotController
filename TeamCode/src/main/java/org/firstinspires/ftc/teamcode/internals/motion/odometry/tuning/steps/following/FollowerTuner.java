package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.following;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

public class FollowerTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;
    private final double DISTANCE_BF = 50; // in
    private final double DISTANCE_HEAD = 48; // in
    private Trajectory bfTrajectoryForward = null;
    private Trajectory bfTrajectoryBackward = null;
    private boolean bfForward = true;
    private TrajectorySequence headTrajectory = null;
    private boolean lastX = false;


    private enum Step {
        INSTRUCT,
        ALIGN_BF,
        BF,
        ALIGN_HEAD,
        HEAD,
        NEXT
    }

    private Step step = Step.INSTRUCT;

    @Override
    public boolean when() {
        return State.followerTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case INSTRUCT:
                AsyncQuestionExecutor.askC1("Now we're going to tune the follower PID controllers. Watch the tutorial at bit.ly/followerpid before starting. It will go over the basics of tuning the follower PID. We're going to tune the values of the HEADING_PID and TRANSLATIONAL_PID settings, so make sure they're open in the Dashboard. If you need to manually reposition the robot during tuning, you can toggle driver control by pressing B on either controller. Press X on either controller to switch between the coarse and fine tuning modes. When you're done tuning, press down on the touchpad on either controller. Select Ok when you're ready.", new String[] {"Ok"}, a -> {
                    step = Step.ALIGN_BF;
                });
                break;
            case ALIGN_BF:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "Use the second controller to drive your bot to the start of a " + Math.ceil(DISTANCE_BF / 24.0) + 2 + " (over " + DISTANCE_BF + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
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
                // toggle logic
                if(Devices.controller1.getX() && !lastX || Devices.controller2.getX() && !lastX) {
                    step = Step.ALIGN_HEAD;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    lastX = true;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }else if(!Devices.controller1.getX() && !Devices.controller2.getX()) {
                    lastX = false;
                }
                exit();
                Item answer = menuManager.runOnce();
                // then we stop if the user's done
                if(answer != null) {
                    if(answer.equals("Ok")) {
                        step = Step.BF;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case BF:
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    DSLogging.log("If you need to manually reposition the robot during tuning, you can toggle " +
                        "driver control by pressing B on either controller. Press X on either controller to switch between the coarse and fine tuning modes. When you're done tuning, press down on the touchpad on either controller.");
                    DSLogging.update();
                    bfTrajectoryForward = driver.trajectoryBuilder(new Pose2d())
                        .forward(DISTANCE_BF)
                        .build();
                    bfTrajectoryBackward = driver.trajectoryBuilder(bfTrajectoryForward.end())
                        .back(DISTANCE_BF)
                        .build();
                }
                // toggle logic
                if(Devices.controller1.getB() || Devices.controller2.getB()) {
                    step = Step.ALIGN_BF;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }
                if(Devices.controller1.getX() && !lastX || Devices.controller2.getX() && !lastX) {
                    step = Step.ALIGN_HEAD;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    lastX = true;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }else if(!Devices.controller1.getX() && !Devices.controller2.getX()) {
                    lastX = false;
                }
                if(exit()) {
                    break;
                }
                // drive
                if(!driver.isBusy()) {
                    if(bfForward) {
                        driver.followTrajectoryAsync(bfTrajectoryForward);
                    }else{
                        driver.followTrajectoryAsync(bfTrajectoryBackward);
                    }
                    bfForward = !bfForward;
                }else{
                    driver.update();
                }
                break;
            case ALIGN_HEAD:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "Use the second controller to drive your bot to the bottom right corner of a " + Math.ceil(DISTANCE_HEAD / 24.0) + 2 + " by " + Math.ceil(DISTANCE_HEAD / 24.0) + 2 + " (over " + DISTANCE_HEAD + " by " + DISTANCE_HEAD + " inch) tile square of field tiles facing forward towards the top right corner of the square, then select Ok.", "Ok");
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
                // toggle logic
                if(Devices.controller1.getX() && !lastX || Devices.controller2.getX() && !lastX) {
                    step = Step.ALIGN_BF;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    lastX = true;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }else if(!Devices.controller1.getX() && !Devices.controller2.getX()) {
                    lastX = false;
                }
                if(exit()) {
                    break;
                }
                Item answer1 = menuManager.runOnce();
                // then we stop if the user's done
                if(answer1 != null) {
                    if(answer1.equals("Ok")) {
                        step = Step.HEAD;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case HEAD:
                if(driver == null) {
                    driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                    DSLogging.log("If you need to manually reposition the robot during tuning, you can toggle " +
                        "driver control by pressing B on either controller. Press X on either controller to switch between the coarse and fine tuning modes. When you're done tuning, press down on the touchpad on either controller.");
                    DSLogging.update();
                    Pose2d startPose = new Pose2d(-DISTANCE_HEAD / 2, -DISTANCE_HEAD / 2, 0);
                    driver.setPoseEstimate(startPose);
                    headTrajectory = driver.trajectorySequenceBuilder(startPose)
                        .forward(DISTANCE_HEAD)
                        .turn(Math.toRadians(90))
                        .forward(DISTANCE_HEAD)
                        .turn(Math.toRadians(90))
                        .forward(DISTANCE_HEAD)
                        .turn(Math.toRadians(90))
                        .forward(DISTANCE_HEAD)
                        .turn(Math.toRadians(90))
                        .completeTrajectory();
                }
                // toggle logic
                if(Devices.controller1.getB() || Devices.controller2.getB()) {
                    step = Step.ALIGN_HEAD;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }
                if(Devices.controller1.getX() && !lastX || Devices.controller2.getX() && !lastX) {
                    step = Step.ALIGN_BF;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    lastX = true;
                    DSLogging.clear();
                    DSLogging.update();
                    break;
                }else if(!Devices.controller1.getX() && !Devices.controller2.getX()) {
                    lastX = false;
                }
                if(exit()) {
                    break;
                }
                // drive
                if(!driver.isBusy()) {
                    driver.followTrajectorySequenceAsync(headTrajectory);
                }else{
                    driver.update();
                }
                break;
            case NEXT:
                State.spline = Affair.PRESENT;
                State.followerTuning = Affair.PAST;
                break;
        }
    }

    private boolean exit() {
        if(Devices.controller1.getTouchpad() || Devices.controller2.getTouchpad()) {
            if(driver != null) {
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
            }
            DSLogging.clear();
            DSLogging.update();
            step = Step.NEXT;
            return true;
        }
        return false;
    }

}
