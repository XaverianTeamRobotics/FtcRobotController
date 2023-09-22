package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.bindings.ControllerBindings;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class BindingsTest extends OperationMode implements TeleOperation {
    String TEST_BUTTON = "binding_test.button";
    String TEST_AXIS = "binding_test.axis";
    @Override
    public void construct() {
        ControllerBindings.addButtonBinding(TEST_BUTTON, ControllerBindings.controller1.buttons.A);
        ControllerBindings.addAxisBinding(TEST_AXIS, ControllerBindings.controller1.axes.rightTrigger);
    }

    @Override
    public void run() {
        if (ControllerBindings.getButton(TEST_BUTTON).getValue()) telemetry.addLine("Button Pressed");
        telemetry.addData("Axis", ControllerBindings.getAxis(TEST_AXIS).getValue());
        telemetry.update();
    }
}
