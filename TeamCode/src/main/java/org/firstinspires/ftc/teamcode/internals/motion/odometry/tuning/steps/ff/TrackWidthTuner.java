package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.ff;

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
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdometrySettingsDashboardConfiguration;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class TrackWidthTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;
    private final double ANGLE = 180; // deg
    private final int NUM_TRIALS = 5;
    private final int DELAY = 1000; // ms
    private String str = "";

    private enum Step {
        ALIGN,
        RUN,
        SHOW,
        NEXT
    }

    private Step step = Step.ALIGN;

    @Override
    public boolean when() {
        return State.driveTrackWidthExperimentalTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to tune the drive track width now. Use the second gamepad to drive your bot to a suitable position to make a 360 degree turn, then select Ok.", "Ok");
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
                    }
                }
                break;
            case RUN:
                DSLogging.log("Tuning track width...");
                DSLogging.update();
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                MovingStatistics trackWidthStats = new MovingStatistics(NUM_TRIALS);
                for(int i = 0; i < NUM_TRIALS; i++) {
                    driver.setPoseEstimate(new Pose2d());

                    // it is important to handle heading wraparounds
                    double headingAccumulator = 0;
                    double lastHeading = 0;

                    driver.turnAsync(Math.toRadians(ANGLE));

                    while(!HardwareGetter.getOpMode().isStopRequested() && driver.isBusy()) {
                        double heading = driver.getPoseEstimate().getHeading();
                        headingAccumulator += Angle.normDelta(heading - lastHeading);
                        lastHeading = heading;

                        driver.update();
                    }

                    double trackWidth = OdometrySettingsDashboardConfiguration.TRACK_WIDTH * Math.toRadians(ANGLE) / headingAccumulator;
                    trackWidthStats.add(trackWidth);

                    Clock.sleep(DELAY);
                }
                str = Misc.formatInvariant("Your effective track width was calculated to be %.2f (SE = %.3f)",
                    trackWidthStats.getMean(),
                    trackWidthStats.getStandardDeviation() / Math.sqrt(NUM_TRIALS));
                // we should clean up things at the end, just to make sure everythings safe
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                DSLogging.clear();
                DSLogging.update();
                step = Step.SHOW;
                break;
            case SHOW:
                AsyncQuestionExecutor.askC1(str + " Set your TRACK_WIDTH to this value, then select Continue.", new String[] {"Continue"}, a -> {
                    step = Step.NEXT;
                });
                break;
            case NEXT:
                State.manualDriveTrackWidthExperimentalTuning = Affair.PRESENT;
                State.driveTrackWidthExperimentalTuning = Affair.PAST;
                break;
        }
    }

}
