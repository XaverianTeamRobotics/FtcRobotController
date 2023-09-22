package org.firstinspires.ftc.teamcode.internals.bindings;

import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;

public class MappableController {
    Gamepad controller;

    protected MappableController(Gamepad controller) {
        this.controller = controller;
        try {
            this.buttons = new Buttons();
            this.axes = new Axes();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public class Buttons {
        public Button A = new Button(controller, Gamepad.class.getMethod("getA"));
        public Button B = new Button(controller, Gamepad.class.getMethod("getB"));
        public Button X = new Button(controller, Gamepad.class.getMethod("getX"));
        public Button Y = new Button(controller, Gamepad.class.getMethod("getY"));

        public Button leftBumper = new Button(controller, Gamepad.class.getMethod("getLeftBumper"));
        public Button rightBumper = new Button(controller, Gamepad.class.getMethod("getRightBumper"));

        public Button leftStickButton = new Button(controller, Gamepad.class.getMethod("getLeftStickButton"));
        public Button rightStickButton = new Button(controller, Gamepad.class.getMethod("getRightStickButton"));

        public Button dPadUp = new Button(controller, Gamepad.class.getMethod("getDpadUp"));
        public Button dPadDown = new Button(controller, Gamepad.class.getMethod("getDpadDown"));
        public Button dPadLeft = new Button(controller, Gamepad.class.getMethod("getDpadLeft"));
        public Button dPadRight = new Button(controller, Gamepad.class.getMethod("getDpadRight"));

        public Button options = new Button(controller, Gamepad.class.getMethod("getOptions"));
        public Button share = new Button(controller, Gamepad.class.getMethod("getShare"));
        public Button start = new Button(controller, Gamepad.class.getMethod("getStart"));

        public Button touchpad = new Button(controller, Gamepad.class.getMethod("getTouchpad"));

        Buttons() throws NoSuchMethodException {
        }
    }
    public class Axes {
        public Axis leftTrigger = new Axis(controller, Gamepad.class.getMethod("getLeftTrigger"));
        public Axis rightTrigger = new Axis(controller, Gamepad.class.getMethod("getRightTrigger"));

        public Axis leftStickY = new Axis(controller, Gamepad.class.getMethod("getLeftStickY"));
        public Axis leftStickX = new Axis(controller, Gamepad.class.getMethod("getLeftStickX"));

        public Axis rightStickY = new Axis(controller, Gamepad.class.getMethod("getRightStickY"));
        public Axis rightStickX = new Axis(controller, Gamepad.class.getMethod("getRightStickX"));

        Axes() throws NoSuchMethodException {
        }
    }

    public Buttons buttons;
    public Axes axes;
}
