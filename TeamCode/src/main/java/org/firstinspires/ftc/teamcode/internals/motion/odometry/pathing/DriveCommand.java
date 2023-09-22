package org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing;

import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;

import java.util.function.Supplier;

public class DriveCommand {

    private DriveCommand() {}

    /**
     * Waits for a callback to return true.
     */
    public static class Wait extends DriveCommand {

        private final Supplier<Boolean> supplier;

        public Wait(Supplier<Boolean> supplier) {
            this.supplier = supplier;
        }

        public Supplier<Boolean> getSupplier() {
            return supplier;
        }

    }

    /**
     * Does something. Don't need to drive anywhere but still need to do stuff? Me.
     */
    public static class Do extends DriveCommand {

        private final Runnable action;

        public Do(Runnable action) {
            this.action = action;
        }

        public Runnable getAction() {
            return action;
        }
    }

    /**
     * Drives a trajectory sequence.
     */
    public static class Drive extends DriveCommand {

        private final TrajectorySequence sequence;

        public Drive(TrajectorySequence sequence) {
            this.sequence = sequence;
        }

        public TrajectorySequence getSequence() {
            return sequence;
        }

    }

    /**
     * <a href="https://www.youtube.com/watch?v=dyIilW_eBjc"><strong>KILL</strong></a>
     */
    protected static class Kill extends DriveCommand {}

}
