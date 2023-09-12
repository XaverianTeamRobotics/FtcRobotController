package org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoBuilder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.sequencesegment.SequenceSegment;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TrajectorySequence {
    private final List<SequenceSegment> sequenceList;
    private final AutonomousDrivetrain drivetrain;
    private final AutoBuilder path;

    public TrajectorySequence(List<SequenceSegment> sequenceList) {
        this(sequenceList, null, null);
    }

    public TrajectorySequence(List<SequenceSegment> sequenceList, AutonomousDrivetrain drivetrain, AutoBuilder path) {
        if (sequenceList.size() == 0) throw new EmptySequenceException();

        this.sequenceList = Collections.unmodifiableList(sequenceList);
        this.drivetrain = drivetrain;
        this.path = path;
        if(this.path != null) {
            path.appendTrajectory(this);
        }
    }

    public Pose2d start() {
        return sequenceList.get(0).getStartPose();
    }

    public Pose2d end() {
        return sequenceList.get(sequenceList.size() - 1).getEndPose();
    }

    public double duration() {
        double total = 0.0;

        for (SequenceSegment segment : sequenceList) {
            total += segment.getDuration();
        }

        return total;
    }

    public SequenceSegment get(int i) {
        return sequenceList.get(i);
    }

    public int size() {
        return sequenceList.size();
    }

    public TrajectorySequence appendWait(Supplier<Boolean> condition) {
        path.appendWait(condition);
        return this;
    }

    public TrajectorySequence appendWait(long ms) {
        path.appendWait(() -> {
            Clock.sleep(ms);
            return true;
        });
        return this;
    }

    public TrajectorySequence appendAction(Runnable action) {
        path.appendAction(action);
        return this;
    }

    public TrajectorySequenceBuilder appendTrajectory() {
        return drivetrain.trajectorySequenceBuilder(end());
    }

    public Auto complete() {
        return path.auto().setPath(path.commands()).setEnd(path.end());
    }
}
