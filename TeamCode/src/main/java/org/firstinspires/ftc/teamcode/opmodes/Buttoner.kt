package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.enableButtonRegistration
import org.firstinspires.ftc.teamcode.internals.hardware.data.GamepadRequestInput
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging

class Buttoner : OperationMode(), TeleOperation {
    override fun construct() {
        enableButtonRegistration()
        Devices.controller1.registerButton(GamepadRequestInput.A, "Testing")
    }

    override fun run() {
        val `val`: Any = Devices.controller1.buttonSearch("Testing")
        val val2 = `val` as Double
        Logging.log(`val`.toString() + "")
        //Devices.controller1.registerButton(GamepadRequestInput.A, "Testing"); // will fail
        Logging.log("Hi")


        //Devices.controller1.buttonSearch("Testing");
        //Devices.controller1.getRightTrigger(); // will fail the test
        Logging.update()
    }
}