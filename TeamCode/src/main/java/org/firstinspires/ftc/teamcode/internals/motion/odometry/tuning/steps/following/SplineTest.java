package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning.steps.following;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

public class SplineTest extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDrivetrain driver = null;
    private String firstMsg = "We're almost done! Now we're going to run the bot along a quintic spline to make sure your odometry runs correctly. Use the second controller to drive your bot to";

    private enum Step {
        ALIGN,
        TEST,
        RECON,
        NEXT
    }

    private Step step = Step.ALIGN;

    @Override
    public boolean when() {
        return State.spline == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, firstMsg + " the bottom right corner of a " + Math.ceil(30 / 24.0) + 2 + " by " + Math.ceil(30 / 24.0) + 2 + " (over " + 30 + " by " + 30 + " inch) tile square of field tiles facing forward towards the top right corner of the square. Open the Dashboard and watch the field to make sure the bot is following the path properly. It should follow an S-shaped path when the test is ran. If something goes wrong, check the troubleshooting guide at bit.ly/splinetest. When you're ready, select Ok.","Ok");
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
                DSLogging.log("Running test...");
                DSLogging.update();
                driver = new AutonomousDrivetrain(HardwareGetter.getHardwareMap());
                Trajectory traj = driver.trajectoryBuilder(new Pose2d())
                    .splineTo(new Vector2d(30, 30), 0)
                    .build();
                driver.followTrajectory(traj);
                Clock.sleep(2000);
                driver.followTrajectory(
                    driver.trajectoryBuilder(traj.end(), true)
                        .splineTo(new Vector2d(0, 0), Math.toRadians(180))
                        .build()
                );
                Clock.sleep(2000);
                driver.setMotorPowers(0, 0, 0, 0);
                driver = null;
                step = Step.RECON;
                DSLogging.clear();
                DSLogging.update();
                break;
            case RECON:
                AsyncQuestionExecutor.askC1("If everything went well, select Continue. Otherwise, attempt to fix the problem and select Test Again. You can find a troubleshooting guide at bit.ly/splinetest. It might be easier to select Continue and restart the tuning process from scratch, so do that if you think its a good idea.", new String[] {"Continue", "Test Again"}, a -> {
                    if(a.equals("Continue")) {
                        step = Step.NEXT;
                    }else{
                        step = Step.ALIGN;
                        firstMsg = "Realign your bot with";
                    }
                });
                break;
            case NEXT:
                State.endTuning = Affair.PRESENT;
                State.spline = Affair.PAST;
        }
    }

}
