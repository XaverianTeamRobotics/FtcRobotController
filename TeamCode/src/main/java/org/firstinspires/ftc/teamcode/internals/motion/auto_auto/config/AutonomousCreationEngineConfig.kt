package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.config

import com.google.gson.Gson
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.log
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging.update
import java.io.File
import java.util.*

class AutonomousCreationEngineConfig {
    companion object {
        val CONFIG_FILE_NAME: String = File(AppUtil.FIRST_FOLDER, "ace-c-config.json").absolutePath

        @JvmStatic
        fun getPreconfiguredConfig(): AutonomousCreationEngineConfig {
            val file = File(CONFIG_FILE_NAME)
            if (!file.exists()) {
                throw IllegalStateException("Preconfigured config file does not exist")
            }
            val gson = Gson()
            val map = gson.fromJson(file.readText(), Map::class.java)
            val teamColor = TeamColor.valueOf(map["teamColor"].toString())
            val startPosition = StartPosition.valueOf(map["startPosition"].toString())
            val autoActionsUncasted = map["autoActions"] as List<*>
            val autoActions = autoActionsUncasted.map { AutonomousAction.valueOf(it.toString()) }
            return AutonomousCreationEngineConfig().apply {
                this.teamColor = teamColor
                this.startPosition = startPosition
                this.autoActions = autoActions
            }
        }
    }
    enum class TeamColor {
        BLUE, RED;
    }

    enum class StartPosition {
        LEFT, RIGHT;
    }

    enum class AutonomousAction {
        PARK_LEFT, PARK_CENTER, PARK_RIGHT, BACKDROP_SCORE, SPIKE_MARK_SCORE, DELAY_1S, DELAY_5S
    }

    var teamColor: TeamColor? = null
        internal set
    var startPosition: StartPosition? = null
        internal set
    var autoActions: List<AutonomousAction>? = null
        internal set

    private val autoEndActions = listOf(AutonomousAction.PARK_LEFT, AutonomousAction.PARK_CENTER, AutonomousAction.PARK_RIGHT)
    private val questionFilters: Array<(AutonomousAction) -> Boolean> = arrayOf(
        { it != AutonomousAction.SPIKE_MARK_SCORE || autoActions!!.isEmpty() }
    )

    private fun enumToName(enumName: String): String {
        return enumName.lowercase(Locale.ROOT).replace("_", " ")
    }

    private fun autoActionToName(autoAction: AutonomousAction): String {
        return enumToName(autoAction.name)
    }

    private fun askBasicInfo() {
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

    fun askQuestions() {
        if (isPreConfigured()) {
            var isPreConfigDesired: Boolean
            var preconfiguredConfig = getPreconfiguredConfig()
            val isPreConfiguredMenu = Questions.askAsyncC2(
                buildString {
                    append("Do you want to use the preconfigured config?")
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
        if (File(CONFIG_FILE_NAME).exists()) {
            try {
                getPreconfiguredConfig()
                return true
            } catch (e: Exception) {
                log("Preconfigured config file exists but is not valid")
                log(e.message)
                update()
                opMode!!.waitFor(3.0)
                return false
            }
        } else {
            return false
        }
    }

    private fun askAutonomousActions() {
        val actionsToPrompt = AutonomousAction.entries.toTypedArray().filter {
            for (filter in questionFilters) {
                if (!filter.invoke(it)) return@filter false
            }
            return@filter true
        }
        val autoActionsMenu = Questions.askAsyncC2(
            "What actions do you want to perform in autonomous?",
            *(actionsToPrompt.map { autoActionToName(it) }.toTypedArray()),
        )
        when (autoActionsMenu.run()?.name) {
            autoActionToName(AutonomousAction.PARK_LEFT) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.PARK_LEFT) }.toList()
            autoActionToName(AutonomousAction.PARK_CENTER) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.PARK_CENTER) }.toList()
            autoActionToName(AutonomousAction.PARK_RIGHT) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.PARK_RIGHT) }.toList()
            autoActionToName(AutonomousAction.BACKDROP_SCORE) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.BACKDROP_SCORE) }.toList()
            autoActionToName(AutonomousAction.SPIKE_MARK_SCORE) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.SPIKE_MARK_SCORE) }.toList()
            autoActionToName(AutonomousAction.DELAY_1S) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.DELAY_1S) }.toList()
            autoActionToName(AutonomousAction.DELAY_5S) -> autoActions = autoActions!!.toMutableList().apply { add(AutonomousAction.DELAY_5S) }.toList()
        }
        if (!autoEndActions.contains(autoActions!!.last())) {
            askAutonomousActions()
        }
    }

    fun saveConfiguration() {
        if (!isValid()) {
            throw IllegalStateException("Config is not valid")
        }
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

    fun isValid(): Boolean {
        return teamColor != null && startPosition != null && autoActions != null
    }

    fun asAutoAutoCreatorConfig(): AutoAutoCreatorConfig {
        return AutoAutoCreatorConfig(
            teamColor!!.ordinal,
            startPosition!!.ordinal,
            autoActions!!.contains(AutonomousAction.BACKDROP_SCORE),
            1,
            autoActions!!.contains(AutonomousAction.SPIKE_MARK_SCORE),
            when (autoActions!!.last()) {
                AutonomousAction.PARK_LEFT -> 0
                AutonomousAction.PARK_CENTER -> 1
                AutonomousAction.PARK_RIGHT -> 2
                else -> 0
            },
        )
    }
}