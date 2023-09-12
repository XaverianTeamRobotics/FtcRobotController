package org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class AutoBuilder {

    private final Auto auto;
    private Pose2d end = null;

    public AutoBuilder(Auto auto) {
        this.auto = auto;
    }

    private final Queue<DriveCommand> commands = new LinkedList<>();

    public void appendTrajectory(TrajectorySequence traj) {
        commands.add(new DriveCommand.Drive(traj));
        end = traj.end();
    }

    public void appendWait(Supplier<Boolean> wait) {
        commands.add(new DriveCommand.Wait(wait));
    }

    public void appendAction(Runnable action) {
        commands.add(new DriveCommand.Do(action));
    }

    public Queue<DriveCommand> commands() {
        return commands;
    }

    public Auto auto() {
        return auto;
    }

    public Pose2d end() {
        return end;
    }

}
