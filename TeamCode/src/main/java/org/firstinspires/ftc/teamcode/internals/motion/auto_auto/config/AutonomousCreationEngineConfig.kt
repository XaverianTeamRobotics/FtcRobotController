package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config

import org.firstinspires.ftc.teamcode.internals.telemetry.Questions
import java.util.*

class AutonomousCreationEngineConfig {
    enum class TeamColor(i: Int) {
        RED(1), BLUE(0)
    }

    enum class StartPosition(i: Int) {
        LEFT(0), RIGHT(1)
    }

    enum class AutonomousActions {
        PARK_LEFT, PARK_CENTER, PARK_RIGHT, BACKDROP_SCORE, SPIKE_MARK_SCORE
    }

    var teamColor: TeamColor? = null
        internal set
    var startPosition: StartPosition? = null
        internal set

    private fun enumToName(enumName: String): String {
        return enumName.lowercase(Locale.ROOT).replace("_", " ")
    }

    private fun autoActionToName(autoAction: AutonomousActions): String {
        return enumToName(autoAction.name)
    }

    fun askBasicInfo() {
        val teamColorMenu = Questions.askAsyncC2("What is your team color?", TeamColor.RED.name, TeamColor.BLUE.name)
        val startPositionMenu = Questions.askAsyncC2("What is your starting position?", StartPosition.LEFT.name, StartPosition.RIGHT.name)

        teamColor = if (teamColorMenu.run()!!.name == TeamColor.RED.name) {
            TeamColor.RED
        } else {
            TeamColor.BLUE
        }

        startPosition = if (startPositionMenu.run()!!.name == StartPosition.LEFT.name) {
            StartPosition.LEFT
        } else {
            StartPosition.RIGHT
        }
    }

    fun askAllQuestions() {
        askBasicInfo()
        askAutonomousActions()
    }

    private fun askAutonomousActions() {
        TODO("Not yet implemented")
    }
}