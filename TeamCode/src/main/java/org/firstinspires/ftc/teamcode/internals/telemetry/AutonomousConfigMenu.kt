package org.firstinspires.ftc.teamcode.internals.telemetry

import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants.showTeleOpObjectiveReminder
import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants.startingCode
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu.MenuBuilder
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager

/*
 * Display a prompt to configure different options
 * Both returns the values AND sets some static variables
 */
fun autoConfigMenu(): AutoConfigResults {
    val startingCodeMenuBuilder = MenuBuilder()
    val blueLeftItem = Item("Blue Left (Code 1)")
    val blueRightItem = Item("Blue Right (Code 2)")
    val redLeftItem = Item("Red Left (Code 3)")
    val redRightItem = Item("Red Right (Code 4)")
    startingCodeMenuBuilder.addItem(blueLeftItem)
    startingCodeMenuBuilder.addItem(blueRightItem)
    startingCodeMenuBuilder.addItem(redLeftItem)
    startingCodeMenuBuilder.addItem(redRightItem)
    startingCodeMenuBuilder.setDescription("Starting Code")

    when (MenuManager(startingCodeMenuBuilder.build(), controller1).run().name) {
        blueLeftItem.name -> startingCode = 1
        blueRightItem.name -> startingCode = 2
        redLeftItem.name -> startingCode = 3
        redRightItem.name -> startingCode = 4
    }

    val showTeleOpObjectiveReminderMenuBuilder = MenuBuilder()
    val yesItem = Item("Yes")
    val noItem = Item("No")
    showTeleOpObjectiveReminderMenuBuilder.addItem(yesItem)
    showTeleOpObjectiveReminderMenuBuilder.addItem(noItem)
    showTeleOpObjectiveReminderMenuBuilder.setDescription("Show TeleOp Objective Reminder")
    showTeleOpObjectiveReminder = MenuManager(showTeleOpObjectiveReminderMenuBuilder.build(), controller1).run().name == yesItem.name

    return AutoConfigResults(startingCode, showTeleOpObjectiveReminder)
}

data class AutoConfigResults(val startingCode: Int, val showTeleOpObjectiveReminder: Boolean)

object UserConfiguredConstants {
    @JvmStatic
    var startingCode: Int = 1
    @JvmStatic
    var showTeleOpObjectiveReminder: Boolean = true
}