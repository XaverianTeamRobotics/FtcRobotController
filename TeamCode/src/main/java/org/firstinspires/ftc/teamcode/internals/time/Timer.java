package org.firstinspires.ftc.teamcode.internals.time;

/**
 * A timer tracks the elapsed time since an arbitrary epoch at a sub-nanosecond resolution.
 */
public class Timer {

    private long nanoTime;
    private String name;

    public Timer(String name) {
        this.name = name;
        nanoTime = System.nanoTime();
    }

    /**
     * Sets the timer's epoch to the current time. Essentially, this resets the timer to 0.
     */
    public void reset() {
        nanoTime = System.nanoTime();
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the amount of time specified has elapsed since the last call to {@link #reset()}. The time must be in seconds. You can specify sub-second time as a decimal; for example 0.020 for 20 milliseconds or 0.000000001 for 1 nanosecond.
     */
    public boolean elapsed(double seconds) {
        return nanoTime / 1e+9D < nanoTime / 1e+9D + seconds;
    }

}
