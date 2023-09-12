package org.firstinspires.ftc.teamcode.internals.telemetry

import com.michaell.looping.ScriptTemplate
import com.michaell.looping.builtin.ConvertToScript
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging
import kotlin.math.min

/**
 * Create a menu in the telemetry interface (using the Logging class) to allow for configuration during a match
 * Use controllers as inputs to navigate the menu
 */
@Deprecated("Use the new MenuManager and Menu classes instead")
class TelemetryMenu {
    var dPadUpHeld: Boolean = false
    var dPadDownHeld: Boolean = false
    var dPadLeftHeld: Boolean = false
    var dPadRightHeld: Boolean = false
    var backHeld: Boolean = false
    var currentMenuIndex: Int = 0
    var currentMenu: MenuItem = MenuItem("Main Menu", MenuItemType.MENU, true)
    lateinit var jloopingScript: ScriptTemplate
    var allMenuItems: MutableList<MenuItem> = mutableListOf()
    var annotation: String = ""

    fun addMenuItem(menuItem: MenuItem) {
        (currentMenu.value as MutableList<MenuItem>).add(menuItem)
        allMenuItems.add(menuItem)

        if (menuItem.type == MenuItemType.MENU) {
            allMenuItems.addAll(menuItem.value as MutableList<MenuItem>)
        }
    }

    fun getMenuItemInCurrentMenu(name: String): MenuItem? {
        for (menuItem in (currentMenu.value as MutableList<MenuItem>)) {
            if (menuItem.name == name) {
                return menuItem
            }
        }
        return null
    }

    fun getMenuItem(name: String): MenuItem? {
        for (menuItem in allMenuItems) {
            if (menuItem.name == name) {
                return menuItem
            }
        }
        return null
    }

    private fun previousMenuItem() {
        if (currentMenuIndex > 0) {
            currentMenuIndex--
        } else {
            currentMenuIndex = (currentMenu.value as MutableList<MenuItem>).size - 1
        }

        currentMenuIndex = min(currentMenuIndex, (currentMenu.value as MutableList<MenuItem>).size - 1)
    }

    private fun nextMenuItem() {
        if (currentMenuIndex < (currentMenu.value as MutableList<MenuItem>).size - 1) {
            currentMenuIndex++
        } else {
            currentMenuIndex = 0
        }
    }

    private fun onDPadUp() {
        previousMenuItem()
    }

    private fun onDPadDown() {
        nextMenuItem()
    }

    private fun onDPadLeft() {
        val item = (currentMenu.value as MutableList<MenuItem>)[currentMenuIndex]
        if (item.type != MenuItemType.MENU) {
            item.decrement()
        }
    }

    private fun onDPadRight() {
        val item = (currentMenu.value as MutableList<MenuItem>)[currentMenuIndex]
        if (item.type != MenuItemType.MENU) {
            item.increment()
        } else {
            item.enterMenu(currentMenu)
            currentMenu = item
        }
    }

    private fun display() {
        AdvancedLogging.clear()
        AdvancedLogging.logText("Telemetry Menu")
        AdvancedLogging.logText(" ")
        AdvancedLogging.logText("Use the D-Pad to navigate the menu")
        AdvancedLogging.logText(" ")
        AdvancedLogging.logText("Current Menu: ${currentMenu.name}")
        AdvancedLogging.logText(" ")
        if (currentMenu.name != "Main Menu") {
            AdvancedLogging.logText("Press CIRLE or B to go back")
            AdvancedLogging.logText(" ")
        }
        for (i in (currentMenu.value as MutableList<MenuItem>).indices) {
            val item = (currentMenu.value as MutableList<MenuItem>)[i]
            var text = "${if (i == currentMenuIndex) "> " else "  "}${item.name}"
            text += item.isEditable.let { if (it) "" else " (read-only)" }
            if (item.type != MenuItemType.MENU) {
                text += ": ${item.value}"
            } else {
                text += "  >>>"
            }
            AdvancedLogging.logText(text)
        }

        AdvancedLogging.logText(" ")
        AdvancedLogging.logText(annotation)
        AdvancedLogging.updateLog()
    }

    private fun onBackButton() {
        currentMenu = currentMenu.previousMenu!!
        currentMenu.exitMenu()
    }

    fun runInBackground() {
        HardwareGetter.jloopingRunner!!.addScript(ConvertToScript("TelemetryMenu", this, null, "listenForControllerInput"))
    }

    fun stopBackground() {
        HardwareGetter.jloopingRunner!!.scripts.remove(jloopingScript)
    }

    fun listenForControllerInput() {
        if (controller1.dpadUp && !dPadUpHeld) {
            onDPadUp()
            dPadUpHeld = true;
        }
        else if (controller1.dpadDown && !dPadDownHeld) {
            onDPadDown()
            dPadDownHeld = true;
        }
        else if (controller1.dpadLeft && !dPadLeftHeld) {
            onDPadLeft()
            dPadLeftHeld = true;
        }
        else if (controller1.dpadRight && !dPadRightHeld) {
            onDPadRight()
            dPadRightHeld = true;
        }
        else if ((controller1.b || controller1.circle) && !backHeld) {
            onBackButton()
            backHeld = true
        }

        if (!controller1.dpadUp) dPadUpHeld = false
        if (!controller1.dpadDown) dPadDownHeld = false
        if (!controller1.dpadLeft) dPadLeftHeld = false
        if (!controller1.dpadRight) dPadRightHeld = false
        if (!controller1.b || !controller1.circle) backHeld = false

        display()
    }
}

class MenuItem(val name: String, val type: MenuItemType, var isEditable: Boolean) {
    var value: Any
        get() = when (type) {
            MenuItemType.BOOLEAN -> field
            MenuItemType.DOUBLE -> field
            MenuItemType.INT -> field
            MenuItemType.MENU -> field
        }
    var previousMenu: MenuItem? = null
    var stepSize: Double = 1.0
        set(value) {
            if (type == MenuItemType.DOUBLE || type == MenuItemType.INT) {
                if (type == MenuItemType.INT) {
                    this.value = value.toInt()
                }
            }
            field = value
        }
    var min: Double = 0.0
        set(value) {
            if (type == MenuItemType.DOUBLE || type == MenuItemType.INT) {
                if (type == MenuItemType.INT) {
                    this.value = value.toInt()
                }
            }
            field = value
        }
    var max: Double = 1000.0
        set(value) {
            if (type == MenuItemType.DOUBLE || type == MenuItemType.INT) {
                if (type == MenuItemType.INT) {
                    this.value = value.toInt()
                }
            }
            field = value
        }

    init {
        value = when (type) {
            MenuItemType.BOOLEAN -> false
            MenuItemType.DOUBLE -> 0.0
            MenuItemType.INT -> 0
            MenuItemType.MENU -> mutableListOf<MenuItem>()
        }
    }

    fun increment() {
        if (isEditable) {
            when (type) {
                MenuItemType.INT -> {
                    value = (value as Int) + stepSize.toInt()
                }

                MenuItemType.DOUBLE -> {
                    value = (value as Double) + stepSize
                }

                MenuItemType.BOOLEAN -> {
                    value = !(value as Boolean)
                }

                else -> {}
            }
        }

//        if (type == MenuItemType.INT || type == MenuItemType.DOUBLE) {
//            // Get value within bounds
//            value = if (type == MenuItemType.DOUBLE) Range.clip(value as Double, min, max) else Range.clip(value as Int, min.toInt(), max.toInt())
//        }
    }

    fun decrement() {
        if (isEditable) {
            when (type) {
                MenuItemType.INT -> {
                    value = (value as Int) - stepSize.toInt()
                }

                MenuItemType.DOUBLE -> {
                    value = (value as Double) - stepSize
                }

                MenuItemType.BOOLEAN -> {
                    value = !(value as Boolean)
                }

                else -> {}
            }
        }
//        if (type == MenuItemType.INT || type == MenuItemType.DOUBLE) {
//            // Get value within bounds
//            if (type == MenuItemType.INT) value = Range.clip(value as Double, min, max)
//            else if (type == MenuItemType.DOUBLE) value = Range.clip(value as Int, min.toInt(), max.toInt())
//        }
    }

    fun enterMenu(previousMenu: MenuItem?) {
        if (type == MenuItemType.MENU) {
            this.previousMenu = previousMenu
        }
    }

    fun exitMenu() {
        if (type == MenuItemType.MENU) {
            previousMenu = null
        }
    }
}

enum class MenuItemType {
    BOOLEAN, DOUBLE, INT, MENU
}