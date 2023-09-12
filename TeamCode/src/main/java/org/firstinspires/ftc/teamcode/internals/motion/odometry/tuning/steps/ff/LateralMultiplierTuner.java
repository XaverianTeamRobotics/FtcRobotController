package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.ff;

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
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class LateralMultiplierTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;
    private String firstMsg = "Mecanum drivetrains often exhibit less torque while strafing, so we're going to tune your strafing multiplier. First, use the second controller to drive";
    private String manMult = "multiplier";
    private boolean redo = false;

    private enum Step {
        ALIGN,
        RUN,
        CALC,
        REALIGN,
        RERUN,
        MANUALCALC,
        MANUALALIGN,
        MANUALTEST,
        NEXT

    }

    private Step step = Step.ALIGN;

    private final double DISTANCE = 60; // in
    private double multiplier = 0.0;
    private double dist = 0.0;

    @Override
    public boolean when() {
        return State.lateralMultiplierTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, firstMsg + " your bot to the start of a " + ((int) Math.ceil(DISTANCE / 24.0) + 2) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing to the right relative to the stretch (270 degrees), then select Ok. (Note: this step of the tuning process is a bit buggy. If it doesn't seem to be working, just say it's good and move on. When you complete tuning, enable the RRQS StrafeTest OpMode and manually tune the multiplier. You'll need to run the ManuallySaveOdo OpMode and reboot the robot every time you edit the multiplier.)", "Ok");
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
                        step = Step.RUN;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                        firstMsg = "Use the second controller to realign ";
                    }
                }
                break;
            case RUN:
                // strafe
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                Trajectory trajectory = driver.trajectoryBuilder(new Pose2d())
                    .strafeRight(DISTANCE)
                    .build();
                driver.followTrajectory(trajectory);
                // calc
                multiplier = Math.abs(DISTANCE) / Math.abs(driver.getPoseEstimate().getY());
                // cleanup
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                step = Step.CALC;
                break;
            case CALC:
                AsyncQuestionExecutor.askC1("Set your LATERAL_MULTIPLIER to " + multiplier + ", then select Ok. Afterwards, we're going to rerun the test to confirm the multiplier is correct.", new String[] {"Ok"}, a -> {
                    step = Step.REALIGN;
                });
                break;
            case REALIGN:
                // the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, firstMsg + " your bot to the start of a " + ((int) Math.ceil(DISTANCE / 24.0) + 2) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing to the right relative to the stretch (270 degrees), then select Ok.", "Ok");
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
                Item ans = menuManager.runOnce();
                // then we stop if the user's done
                if(ans != null) {
                    if(ans.equals("Ok")) {
                        step = Step.RERUN;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case RERUN:
            case MANUALTEST:
                // strafe
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                driver.setPoseEstimate(new Pose2d(0, 0, 0));
                Trajectory traj2 = driver.trajectoryBuilder(new Pose2d())
                    .strafeRight(DISTANCE)
                    .build();
                driver.followTrajectory(traj2);
                dist = driver.getPoseEstimate().getY();
                // cleanup
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                step = Step.MANUALCALC;
                break;
            case MANUALCALC:
                if(!redo) {
                    AsyncQuestionExecutor.askC1("With the " + manMult + " applied, your bot drove a distance of " + dist + " inches. If this is accurate to a few percent, you can move on. Otherwise, select Reconfigure to manually bump your multiplier higher or lower.", new String[] {"Continue", "Reconfigure"}, a -> {
                        if(a.equals("Continue")) {
                            step = Step.NEXT;
                        }else{
                            redo = true;
                            Clock.sleep(1000);
                        }
                    });
                }else{
                    AsyncQuestionExecutor.askC1("Manually bump your multiplier now, then select Ok.", new String[] {"Ok"}, b -> {
                        manMult = "updated multiplier";
                        redo = false;
                        step = Step.MANUALALIGN;
                    });
                }
                break;
            case MANUALALIGN:
                // the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, firstMsg + " your bot to the start of a " + ((int) Math.ceil(DISTANCE / 24.0) + 2) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing to the right relative to the stretch (270 degrees), then select Ok.", "Ok");
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
                Item answe = menuManager.runOnce();
                // then we stop if the user's done
                if(answe != null) {
                    if(answe.equals("Ok")) {
                        step = Step.MANUALTEST;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            // cleanup
            case NEXT:
                AsyncQuestionExecutor.askC1("Your multiplier has now been tuned properly. Select Ok when you're ready to move on to the next step.", new String[] {"Ok"}, a -> {
                    State.driveTrackWidthExperimentalTuning = Affair.PRESENT;
                    State.lateralMultiplierTuning = Affair.PAST;
                });
                break;
        }
    }

}
