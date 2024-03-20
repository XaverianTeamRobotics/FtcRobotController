package org.firstinspires.ftc.teamcode.internals.motion.auto_auto

import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer

class AutonomousReportGenerator(private val timerName: String) {
    private val timer: Timer
        get() {
            return Clock.get(timerName)
        }
    val actions: List<TimedMessage>
        get() = mutableActions.toList()
    private val mutableActions = mutableListOf<TimedMessage>()

    fun generateReport(): String {
        return buildString {
            for (msg in mutableActions) {
                appendLine(msg.toString())
            }
        }.trim('\n')
    }

    fun markTime(action: String) {
        val msg = TimedMessage(action, timer.time)
        mutableActions.add(msg)
    }
}

data class TimedMessage(val action: String, val time: Double)
