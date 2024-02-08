package org.firstinspires.ftc.teamcode.internals.telemetry

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager

/**
 * The Questions framework allows a human driver to answer multiple-choice questions prompted by the robot via an interface on the DS.
 */
object Questions {
    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking.
     * @param prompt The question itself.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @return The answer of the question.
     */
    fun ask(prompt: Menu?, input: Gamepad?): Item? {
        return MenuManager(prompt!!, input!!).run()
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    fun ask(input: Gamepad?, question: String?, vararg options: String?): Item? {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), input!!).run()
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #1.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    fun askC1(prompt: Menu?): Item {
        return MenuManager(prompt!!, Devices.controller1).run()!!
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #1.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    @JvmStatic
    fun askC1(question: String?, vararg options: String?): Item {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), Devices.controller1).run()!!
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #2.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    fun askC2(prompt: Menu): Item {
        return MenuManager(prompt, Devices.controller2).run()!!
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #2.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    fun askC2(question: String?, vararg options: String?): Item? {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), Devices.controller2).run()
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous.
     * @param prompt The question itself.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @return The answer of the question.
     */
    fun askAsync(prompt: Menu, input: Gamepad): MenuManager {
        return MenuManager(prompt, input)
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    @JvmStatic
    fun askAsync(input: Gamepad, question: String?, vararg options: String?): MenuManager {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), input)
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #1.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    fun askAsyncC1(prompt: Menu): MenuManager {
        return MenuManager(prompt, Devices.controller1)
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #1.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    @JvmStatic
    fun askAsyncC1(question: String?, vararg options: String?): MenuManager {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), Devices.controller1)
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #2.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    fun askAsyncC2(prompt: Menu): MenuManager {
        return MenuManager(prompt, Devices.controller2)
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #2.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    fun askAsyncC2(question: String?, vararg options: String?): MenuManager {
        val menuBuilder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            menuBuilder.addItem(option)
        }
        return MenuManager(menuBuilder.build(), Devices.controller2)
    }
}
