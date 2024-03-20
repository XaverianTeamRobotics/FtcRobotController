package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer

class AutonomousReportGenerator(private val timerName: String) {
    private val timer: Timer
        get() {
            return Clock.get(timerName)
        }

    fun generateReport(): String {
        TODO("Should return a string containing a breakdown of the times for each action in the autonomous routine.")
    }

    fun markTime(action: String) {
        TODO("Should mark that the action has been completed at the current time.")
    }
}
