package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config

import org.firstinspires.ftc.teamcode.internals.telemetry.Questions
import java.util.*

class AutonomousCreationEngineConfig {
    companion object {
        const val CONFIG_FILE_NAME = "ace-c-preconfigured.json"

        @JvmStatic
        fun getPreconfiguredConfig(): AutonomousCreationEngineConfig {
            TODO("Not yet implemented")
        }
    }
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
        if (isPreConfigured()) {
            var isPreConfigDesired: Boolean
            var preconfiguredConfig = getPreconfiguredConfig()
            val isPreConfiguredMenu = Questions.askAsyncC2(
                buildString {
                    append("Do you want to use the preconfigured config?")
                    appendLine()
                    append(preconfiguredConfig.toString())
                },
                "Yes", "No"
            )
            isPreConfigDesired = isPreConfiguredMenu.run()!!.name == "Yes"
            if (isPreConfigDesired) {
                copyFrom(preconfiguredConfig)
                return
            }
        }
        askBasicInfo()
        askAutonomousActions()
    }

    private fun copyFrom(preconfiguredConfig: AutonomousCreationEngineConfig) {
        teamColor = preconfiguredConfig.teamColor
        startPosition = preconfiguredConfig.startPosition
    }

    private fun isPreConfigured(): Boolean {
        TODO("Not yet implemented")
    }

    private fun askAutonomousActions() {
        TODO("Not yet implemented")
    }

    fun saveAsPreconfigured() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return buildString {
            append("Autonomous Creation Engine Config")
            appendLine()
            append("Team Color=")
            append(teamColor)
            appendLine()
            append("Start Position=")
            append(startPosition)
        }
    }
}