package org.firstinspires.ftc.teamcode.internals.misc;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

import java.util.function.Consumer;

/**
 * Abstracts an asynchronous question to a single method resembling a callback in other languages. Designed to be ran forever. It will handle itself. Only supports one asynchronous question at a time&mdash;the previous question must be answered before the next one is handled.
 */
public class AsyncQuestionExecutor {
    
    private static MenuManager menuManager = null;

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void ask(Menu prompt, Gamepad input, Consumer<Item> callback) {
        if(menuManager == null) {
            menuManager = new MenuManager(prompt, input);
        }
        Item answer = menuManager.runOnce();
        if(answer != null) {
            callback.accept(answer);
            clear();
        }
    }

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void ask(Gamepad input, String question, String[] options, Consumer<Item> callback) {
        Menu.MenuBuilder builder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            builder.addItem(option);
        }
        ask(builder.build(), input, callback);
    }

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void askC1(Menu prompt, Consumer<Item> callback) {
        ask(prompt, Devices.controller1, callback);
    }

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void askC1(String question, String[] options, Consumer<Item> callback) {
        ask(Devices.controller1, question, options, callback);

    }

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void askC2(Menu prompt, Consumer<Item> callback) {
        ask(prompt, Devices.controller2, callback);
    }

    /**
     * If this is the first call of this method since {@link #clear()} (which will execute after a question is answered automatically), it will generate the question and update it once. On every subsequent call, it will update the question until the question has been answered, in which case the callback will be supplied with the answer and executed.
     */
    public static void askC2(String question, String[] options, Consumer<Item> callback) {
        ask(Devices.controller2, question, options, callback);
    }
    
    private static void clear() {
        menuManager = null;
    }

}
