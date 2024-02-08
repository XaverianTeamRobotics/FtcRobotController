package org.firstinspires.ftc.teamcode.internals.telemetry.graphics

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.opMode
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.clear
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.log
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging.update

/**
 * A MenuManager is a little MVC framework which allows a driver to have simple interactions with the robot via a GUI.
 */
class MenuManager(private val MENU: Menu, private val GAMEPAD: Gamepad) {
    private val timestamps = LongArray(5)
    private var up = false
    private var down = false
    private var a = false
    private var result: Item? = null

    init {
        val time = System.currentTimeMillis()
        timestamps[0] = time
        timestamps[1] = time
        timestamps[2] = time
        timestamps[3] = time
        timestamps[4] = time
    }

    /**
     * Processes menu input.
     */
    private fun input() {
        if (up && !GAMEPAD.dpadUp) {
            MENU.selectItem(true)
            up = false
        } else if (GAMEPAD.dpadUp) {
            up = true
        }
        if (down && !GAMEPAD.dpadDown) {
            MENU.selectItem(false)
            down = false
        } else if (GAMEPAD.dpadDown) {
            down = true
        }
        if (a && !GAMEPAD.a) {
            result = MENU.clickSelectedItem()
            a = false
        } else if (GAMEPAD.a) {
            a = true
        }
    }

    /**
     * Draws a new frame.
     */
    private fun frame() {
        val frame = MENU.draw()
        clear()
        log(frame)
        update()
    }

    /**
     * Displays the menu until the OpMode has been stopped or a choice has been made.
     * @return The choice of the user.
     */
    fun run(): Item? {
        while (!opMode!!.isStopRequested) {
            frame()
            input()
            if (result != null) {
                clear()
                log("")
                update()
                return result
            }
        }
        return null
    }

    /**
     * Displays and updates the menu once until the OpMode has been stopped or a choice has been made.
     * @return The choice of the user.
     */
    fun runOnce(): Item? {
        if (!opMode!!.isStopRequested) {
            frame()
            input()
            if (result != null) {
                clear()
                log("")
                update()
                return result
            }
        }
        return null
    }
}