package org.firstinspires.ftc.teamcode.features;


import org.firstinspires.ftc.teamcode.internals.documentation.ButtonName;
import org.firstinspires.ftc.teamcode.internals.documentation.ButtonUsage;
import org.firstinspires.ftc.teamcode.internals.documentation.ControllerName;
import org.firstinspires.ftc.teamcode.internals.documentation.ReferableButtonUsage;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput;

/**
 * This sets up the airplane launcher servo and uses it to launch the plane.
 * <p>
 * Connections: A servo in port 6 to launch and in port 7 to change the height and a controller1.
 * <p>
 * Controls: Press triangle to launch the plane. Press the left bumpet to move the launcher down and
 * the right bumper to move it up.
 */
@ReferableButtonUsage(referableAs = "AirplaneLauncher")
@ButtonUsage(button = ButtonName.Y, description = "Launch plane", controller = ControllerName.CONTROLLER_1)
@ButtonUsage(button = ButtonName.LEFT_BUMPER, description = "Move plane launcher down", controller = ControllerName.CONTROLLER_1)
@ButtonUsage(button = ButtonName.RIGHT_BUMPER, description = "Move plane launcher up", controller = ControllerName.CONTROLLER_1)
public class AirplaneLauncher extends Feature implements Buildable {
    @Override
    public void build() {
//        Devices.enableButtonRegistration();
//        Devices.controller1.registerButton(GamepadRequestInput.TRIANGLE, "Launch plane");
//        Devices.controller1.registerButton(GamepadRequestInput.LEFT_BUMPER, "Move plane launcher down");
//        Devices.controller1.registerButton(GamepadRequestInput.RIGHT_BUMPER, "Move plane launcher up");
        Devices.servo6.setPosition(35.0);
    }

    @Override
    public void loop() {
        if (Devices.controller1.getTriangle()) {
            Devices.servo6.setPosition(0.0);
        }

        if (Devices.controller1.getLeftBumper()) {
            Devices.servo7.setPosition(0);
        } else if (Devices.controller1.getRightBumper()) {
            Devices.servo7.setPosition(100);
        } else {
            Devices.servo7.setPosition(50);
        }
    }
}
