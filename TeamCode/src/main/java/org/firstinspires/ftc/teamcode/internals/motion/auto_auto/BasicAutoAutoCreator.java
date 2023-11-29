package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.BlueLeftToLeftBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.paths.RedRightToRightBackdrop;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;
import org.firstinspires.ftc.teamcode.opmodes.ScrimmageBotCenterstage1;

import java.util.UUID;

public class BasicAutoAutoCreator extends OperationMode implements AutonomousOperation {
    private AutoAutoCreatorConfig config;
    Timer time;
    AutoRunner runner;

    @Override
    public Class<? extends OperationMode> getNext() {
        return ScrimmageBotCenterstage1.class;
    }

    @Override
    public void construct() {
        time = Clock.make(UUID.randomUUID().toString());
        config = new AutoAutoCreatorConfig();
        config.askQuestions();
        if (!config.isValid()) throw new RuntimeException("Invalid auto auto config");
        Pose2d start = config.getTeamColor() == 0 ? new Pose2d(12, 64.50, Math.toRadians(-90.00)) : new Pose2d(12, -64.50, Math.toRadians(90.00));
        Auto auto = new Auto(start);
        TrajectorySequenceBuilder builder = auto.begin();
        // TEMPORARY!!!!
        if (config.getTeamColor() == 0) {
            builder = new BlueLeftToLeftBackdrop().addPathSegment(builder);
        } else {
            builder = new RedRightToRightBackdrop().addPathSegment(builder);
        }

        auto = builder.completeTrajectory().complete();

        runner = new AutoRunner(auto, auto.getDrivetrain(), null, null, null);
    }

    @Override
    public void run() {
        runner.run();
    }
}
