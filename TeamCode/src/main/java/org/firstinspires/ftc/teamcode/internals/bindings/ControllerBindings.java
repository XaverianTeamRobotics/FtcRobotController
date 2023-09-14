package org.firstinspires.ftc.teamcode.internals.bindings;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

import java.util.HashMap;

/**
 * Bind actions to buttons on the controller and have them changed on a per-opmode basis
 * <p>
 * Actions that would like to use this API should provide a string ID unique to that action.
 * For example, one could be "mecanum.X" and "mecanum.Y". This should be stored as a variable, like
 * `MAIN_AXIS` or something that could be passed as an input to the ControllerBindings class.
 */
public class ControllerBindings {
    private static HashMap<String, Button> buttonMappings = new HashMap<>();
    private static HashMap<String, Axis> axisMappings = new HashMap<>();

    public static MappableController controller1;
    public static MappableController controller2;

    public static void initialize() {
        controller1 = new MappableController(Devices.controller1);
        controller2 = new MappableController(Devices.controller2);
    }

    public static void addAxisBinding(String action, Axis axis) {
        axisMappings.put(action, axis);
    }

    public static void addButtonBinding(String action, Button button) {
        buttonMappings.put(action, button);
    }

    public static Axis getAxis(String action) {
        return axisMappings.get(action);
    }

    public static Button getButton(String action) {
        return buttonMappings.get(action);
    }
}
