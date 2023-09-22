package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.ff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdometrySettingsDashboardConfiguration;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.ConstantUtils;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.LoggingUtil;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.RegressionUtil;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

import java.util.ArrayList;
import java.util.List;

public class AutoFeedforwardTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;

    private final double MAX_POWER = 0.7;
    private final double DISTANCE = 100; // in
    String result;

    private enum Step {
        ALIGN_AUTO,
        AUTO,
        SHOW,
        NEXT
    }

    private Step step = Step.ALIGN_AUTO;

    @Override
    public boolean when() {
        return State.autoFeedforwardTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN_AUTO:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to automatically tune the kV and kStatic feedforward values. First, use the second controller to drive your bot to the start of a " + ((int) Math.ceil(DISTANCE / 24.0) + 2) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
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
                        step = Step.AUTO;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case AUTO:
                // i would rather this be async, buuuuuuut the logic works out a bit easier this way and tbqh accuracy is so important here im a bit worried that pausing execution for other vthreads would be bad
                // therefore, synchronous!
                DSLogging.log("Tuning kV and kStatic...");
                DSLogging.update();
                // inits
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                NanoClock clock = NanoClock.system();
                double maxVel = ConstantUtils.rpmToVelocity(OdometrySettingsDashboardConfiguration.MAX_RPM);
                double finalVel = MAX_POWER * maxVel;
                double accel = (finalVel * finalVel) / (2.0 * DISTANCE);
                double rampTime = Math.sqrt(2.0 * DISTANCE / accel);
                List<Double> timeSamples = new ArrayList<>();
                List<Double> positionSamples = new ArrayList<>();
                List<Double> powerSamples = new ArrayList<>();
                // begin driving
                driver.setPoseEstimate(new Pose2d());
                double startTime = clock.seconds();
                while(true) {
                    // slowly speed up and take measurements along the way
                    double elapsedTime = clock.seconds() - startTime;
                    if (elapsedTime > rampTime) {
                        break;
                    }
                    double vel = accel * elapsedTime;
                    double power = vel / maxVel;
                    timeSamples.add(elapsedTime);
                    positionSamples.add(driver.getPoseEstimate().getX());
                    powerSamples.add(power);
                    driver.setDrivePower(new Pose2d(power, 0.0, 0.0));
                    driver.updatePoseEstimate();
                }
                // stop and calculate the regression. this is beyond my math skills. i dont really know whats happening here
                driver.setDrivePower(new Pose2d(0.0, 0.0, 0.0));
                RegressionUtil.RampResult rampResult = RegressionUtil.fitRampData(
                    timeSamples, positionSamples, powerSamples, true,
                    LoggingUtil.getLogFile(Misc.formatInvariant(
                        "DriveRampRegression-%d.csv", System.currentTimeMillis())));
                // aaaaaaand cleanup
                result = Misc.formatInvariant("Your results are a kV of %.5f and a kStatic of %.5f (R^2 = %.2f).",
                    rampResult.kV, rampResult.kStatic, rampResult.rSquare);
                driver = null;
                step = Step.SHOW;
                DSLogging.clear();
                DSLogging.update();
                break;
            case SHOW:
                AsyncQuestionExecutor.askC1(result + " Set your kV and kStatic settings to these numbers, then select Continue.", new String[] {"Continue"}, a -> {
                    step = Step.NEXT;
                });
                break;
            case NEXT:
                State.manualFeedforwardTuner = Affair.PRESENT;
                State.autoFeedforwardTuner = Affair.PAST;
        }
    }

}
