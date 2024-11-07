package org.firstinspires.ftc.teamcode.internals

/**
 * A simple clock that can be used to measure time in seconds, milliseconds, or nanoseconds
 * @param startTime the time to start the clock at
 * @property seconds the time in seconds since the clock was started
 * @property milliseconds the time in milliseconds since the clock was started
 * @property nanoseconds the time in nanoseconds since the clock was started
 */
class Clock(private val startTime: Long = 0) {
    val seconds: Double
        get() = (System.nanoTime() - startTime) / 1e9

    val milliseconds: Double
        get() = (System.nanoTime() - startTime) / 1e6

    val nanoseconds: Long
        get() = System.nanoTime() - startTime

    fun hasTimeElapsedMs(time: Long): Boolean = milliseconds >= time
    fun hasTimeElapsedS(time: Double): Boolean = seconds >= time
    fun hasTimeElapsedNs(time: Double): Boolean = nanoseconds >= time
}