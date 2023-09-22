package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.ff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
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
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

public class ManualTrackWidthTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;
    private final double ANGLE = 180; // deg
    private double heading = 0.0;
    private String str = "";

    private enum Step {
        ALIGN,
        TEST,
        SHOW,
        TUNE,
        NEXT
    }

    private Step step = Step.ALIGN;

    @Override
    public boolean when() {
        return State.manualDriveTrackWidthExperimentalTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to confirm the drive track width was tuned properly. Use the second gamepad to drive your bot to a suitable position to make a 360 degree turn, then select Ok.", "Ok");
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
                DSLogging.log("Testing...");
                DSLogging.update();
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                driver.turn(Math.toRadians(ANGLE));
                heading = driver.getPoseEstimate().getHeading();
                if(Math.abs(Math.abs(ANGLE) - Math.abs(heading)) <= 3) {
                    str = " Since this is accurate to ±3 degrees of " + ANGLE + ", I recommend selecting Continue to move on to the next step, although you may select Reconfigure to manually edit your drive track width.";
                }else{
                    str = " Since this isn't accurate to ±3 degrees of " + ANGLE + ", I recommend selecting Reconfigure to manually edit your drive track width.";
                }
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                DSLogging.clear();
                DSLogging.update();
                step = Step.SHOW;
                break;
            case SHOW:
                Item i = Questions.askC1("Your robot turned " + heading + " degrees when asked to turn " + ANGLE + " degrees." + str, "Continue", "Reconfigure");
                heading = 0.0;
                str = "";
                if(i.equals("Continue")) {
                    step = Step.NEXT;
                }else{
                    step = Step.TUNE;
                }
                break;
            case TUNE:
                AsyncQuestionExecutor.askC1("If your heading is lower than " + ANGLE + ", raise TRACK_WIDTH. If it's higher, lower TRACK_WIDTH. Select Continue when you're ready to retest your track width.", new String[] {"Continue"}, a -> {
                    step = Step.TEST;
                });
                break;
            case NEXT:
                State.followerTuning = Affair.PRESENT;
                State.manualDriveTrackWidthExperimentalTuning = Affair.PAST;
                break;
        }
    }

}
