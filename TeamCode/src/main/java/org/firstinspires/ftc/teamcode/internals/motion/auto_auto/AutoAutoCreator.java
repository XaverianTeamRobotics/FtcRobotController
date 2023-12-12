package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;
import org.firstinspires.ftc.teamcode.opmodes.ScrimmageBotCenterstage1;

import java.util.UUID;

import static java.lang.Math.toRadians;

public class AutoAutoCreator extends OperationMode implements AutonomousOperation {
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
        AutoNoNavigationZones.addCenterstageDefaults();

        double y = (config.getTeamColor() == 0 ? 1 : -1) * 64.50;
        double rot = config.getTeamColor() == 0 ? toRadians(-90.00) : toRadians(90.00);
        boolean xStartingPos = config.getStartingPosition() == 0;
        if (config.getTeamColor() == 1) xStartingPos = !xStartingPos;
        double x = xStartingPos ? 12 : -36;
        Pose2d start = new Pose2d(x, y, rot);

        TrajectorySequenceBuilder builder = new Auto(start).begin();

        builder.lineTo(new Vector2d(0, 0));

        Auto auto = builder.completeTrajectory().complete();

        runner = new AutoRunner(auto, auto.getDrivetrain(), null, null, null);
    }

    @Override
    public void run() {
        runner.run();
    }
}
