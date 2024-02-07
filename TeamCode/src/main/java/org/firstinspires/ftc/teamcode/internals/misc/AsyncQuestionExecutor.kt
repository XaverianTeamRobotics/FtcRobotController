package org.firstinspires.ftc.teamcode.internals.misc

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager
import java.util.function.Consumer

/**
 * Abstracts an asynchronous question to a single method resembling a callback in other languages. Designed to be ran forever. It will handle itself. Only supports one asynchronous question at a timethe previous question must be answered before the next one is handled.
 */
object AsyncQuestionExecutor {
    private var menuManager: MenuManager? = null

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    @JvmStatic
    fun ask(prompt: Menu?, input: Gamepad?, callback: Consumer<Item?>) {
        if (menuManager == null) {
            menuManager = MenuManager(prompt!!, input!!)
        }
        val answer = menuManager!!.runOnce()
        if (answer != null) {
            callback.accept(answer)
            clear()
        }
    }

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    @JvmStatic
    fun ask(input: Gamepad?, question: String?, options: Array<String?>, callback: Consumer<Item?>) {
        val builder = Menu.MenuBuilder().setDescription(question)
        for (option in options) {
            builder.addItem(option)
        }
        ask(builder.build(), input, callback)
    }

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    @JvmStatic
    fun askC1(prompt: Menu?, callback: Consumer<Item?>) {
        ask(prompt, Devices.controller1, callback)
    }

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    @JvmStatic
    fun askC1(question: String?, options: Array<String?>, callback: Consumer<Item?>) {
        ask(Devices.controller1, question, options, callback)
    }

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    fun askC2(prompt: Menu?, callback: Consumer<Item?>) {
        ask(prompt, Devices.controller2, callback)
    }

    /**
     * If this is the first call of this method since [.clear] (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    fun askC2(question: String?, options: Array<String?>, callback: Consumer<Item?>) {
        ask(Devices.controller2, question, options, callback)
    }

    private fun clear() {
        menuManager = null
    }
}
