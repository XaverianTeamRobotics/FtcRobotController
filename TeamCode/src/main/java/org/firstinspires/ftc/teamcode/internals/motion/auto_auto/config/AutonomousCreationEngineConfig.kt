package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config

import com.google.gson.Gson
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions
import java.io.File
import java.util.*

class AutonomousCreationEngineConfig {
    companion object {
        const val CONFIG_FILE_NAME = "ace-c-preconfigured.json"

        @JvmStatic
        fun getPreconfiguredConfig(): AutonomousCreationEngineConfig {
            val file = File(CONFIG_FILE_NAME)
            if (!file.exists()) {
                throw IllegalStateException("Preconfigured config file does not exist")
            }
            val gson = Gson()
            val map = gson.fromJson(file.readText(), Map::class.java)
            val teamColor = map["teamColor"] as TeamColor
            val startPosition = map["startPosition"] as StartPosition
            val autoActions = map["autoActions"] as List<AutonomousActions>
            return AutonomousCreationEngineConfig().apply {
                this.teamColor = teamColor
                this.startPosition = startPosition
                this.autoActions = autoActions
            }
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
    var autoActions: List<AutonomousActions>? = null
        internal set

    private val autoEndActions = listOf(AutonomousActions.PARK_LEFT, AutonomousActions.PARK_CENTER, AutonomousActions.PARK_RIGHT)

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
        if (autoActions == null) autoActions = listOf()
        askBasicInfo()
        askAutonomousActions()
    }

    private fun copyFrom(preconfiguredConfig: AutonomousCreationEngineConfig) {
        teamColor = preconfiguredConfig.teamColor
        startPosition = preconfiguredConfig.startPosition
        autoActions = preconfiguredConfig.autoActions
    }

    private fun isPreConfigured(): Boolean {
        TODO("Not yet implemented")
    }

    private fun askAutonomousActions() {
        val autoActionsMenu = Questions.askAsyncC2(
            "What actions do you want to perform in autonomous?",
            autoActionToName(AutonomousActions.PARK_LEFT),
            autoActionToName(AutonomousActions.PARK_CENTER),
            autoActionToName(AutonomousActions.PARK_RIGHT),
            autoActionToName(AutonomousActions.BACKDROP_SCORE),
            autoActionToName(AutonomousActions.SPIKE_MARK_SCORE)
        )
        when (autoActionsMenu.run()!!.name) {
            autoActionToName(AutonomousActions.PARK_LEFT) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousActions.PARK_LEFT) }.toList()
            autoActionToName(AutonomousActions.PARK_CENTER) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousActions.PARK_CENTER) }.toList()
            autoActionToName(AutonomousActions.PARK_RIGHT) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousActions.PARK_RIGHT) }.toList()
            autoActionToName(AutonomousActions.BACKDROP_SCORE) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousActions.BACKDROP_SCORE) }.toList()
            autoActionToName(AutonomousActions.SPIKE_MARK_SCORE) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousActions.SPIKE_MARK_SCORE) }.toList()
        }
        if (!autoEndActions.contains(autoActions!!.last())) {
            askAutonomousActions()
        }
    }

    fun saveConfiguration() {
        val file = File(CONFIG_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(toJsonString())
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
            appendLine()
            append("Autonomous Actions=")
            append(autoActions)
        }
    }

    fun toJsonString(): String {
        val gson = Gson()
        val map = mapOf(
            "teamColor" to teamColor,
            "startPosition" to startPosition,
            "autoActions" to autoActions
        )
        return gson.toJson(map)
    }
}