package org.firstinspires.ftc.teamcode.internals.telemetry;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

/**
 * The Questions framework allows a human driver to answer multiple-choice questions prompted by the robot via an interface on the DS.
 */
public class Questions {

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking.
     * @param prompt The question itself.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @return The answer of the question.
     */
    public static Item ask(Menu prompt, Gamepad input) {
        return new MenuManager(prompt, input).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static Item ask(Gamepad input, String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), input).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #1.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    public static Item askC1(Menu prompt) {
        return new MenuManager(prompt, Devices.controller1).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #1.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static Item askC1(String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), Devices.controller1).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #2.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    public static Item askC2(Menu prompt) {
        return new MenuManager(prompt, Devices.controller2).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is blocking. This method accepts input from controller #2.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static Item askC2(String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), Devices.controller2).run();
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous.
     * @param prompt The question itself.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @return The answer of the question.
     */
    public static MenuManager askAsync(Menu prompt, Gamepad input) {
        return new MenuManager(prompt, input);
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static MenuManager askAsync(Gamepad input, String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), input);
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #1.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    public static MenuManager askAsyncC1(Menu prompt) {
        return new MenuManager(prompt, Devices.controller1);
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #1.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static MenuManager askAsyncC1(String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), Devices.controller1);
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #2.
     * @param prompt The question itself.
     * @return The answer of the question.
     */
    public static MenuManager askAsyncC2(Menu prompt) {
        return new MenuManager(prompt, Devices.controller2);
    }

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button. This method is non-blocking. The display must be updated manually when asynchronous. This method accepts input from controller #2.
     * @param question The question itself.
     * @param options The options the user can choose.
     * @return The answer of the question.
     */
    public static MenuManager askAsyncC2(String question, String... options) {
        Menu.MenuBuilder menuBuilder = new Menu.MenuBuilder().setDescription(question);
        for(String option : options) {
            menuBuilder.addItem(option);
        }
        return new MenuManager(menuBuilder.build(), Devices.controller2);
    }

}
