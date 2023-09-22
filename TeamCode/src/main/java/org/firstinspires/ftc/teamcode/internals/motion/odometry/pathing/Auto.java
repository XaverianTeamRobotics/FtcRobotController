package org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;

import java.util.Queue;

public class Auto {

    private final Pose2d start;
    private final AutonomousDrivetrain drivetrain;
    private Queue<DriveCommand> commands = null;
    private Pose2d end;

    public Auto(Pose2d start) {
        this.start = start;
        drivetrain = new AutonomousDrivetrain(new AutoBuilder(this));
        drivetrain.setPoseEstimate(start);
        end = start;
    }

    public Auto(Pose2d start, HardwareMap hardwareMap) {
        this.start = start;
        drivetrain = new AutonomousDrivetrain(hardwareMap, new AutoBuilder(this));
        drivetrain.setPoseEstimate(start);
        end = start;
    }

    public TrajectorySequenceBuilder begin() {
        return drivetrain.trajectorySequenceBuilder(start);
    }

    public Auto setPath(Queue<DriveCommand> commands) {
        this.commands = commands;
        return this;
    }

    public Queue<DriveCommand> path() {
        return commands;
    }

    public AutonomousDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public Auto setEnd(Pose2d end) {
        this.end = end;
        return this;
    }

    public Pose2d end() {
        return end;
    }

}
