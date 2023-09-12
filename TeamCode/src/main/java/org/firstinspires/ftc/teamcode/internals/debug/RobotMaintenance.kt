package org.firstinspires.ftc.teamcode.internals.debug

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Motor
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging

/*
 * A menu to allow the hardware team to quickly check basic functionality, like the installation of motors and servos.
 */
class RobotMaintenance: OperationMode(), TeleOperation {
    lateinit var rootMenu: Menu
    lateinit var motorMenu: Menu
    lateinit var stickSelectMenu: Menu

    val leftStick = mutableSetOf<Motor>()
    val rightStick = mutableSetOf<Motor>()

    var allowMainMenuDraw = true
    override fun construct() {
        DSLogging.log("Robot Maintenance Menu")
        DSLogging.log("A quick debugging menu")
        DSLogging.log("Press RUN to start")
        DSLogging.update()

        // Build the menu and its tree
        rootMenu = Menu.MenuBuilder()
            .addItem("Test Motors/Servos")
            .addItem("Test Sensors")
            .addItem("Test Vision")
            .addItem("View Active Mappings")
            .setDescription("Robot Maintenance Menu - Root Menu")
            .build()

        // Build the motor menu
        motorMenu = Menu.MenuBuilder()
            .addItem("0")
            .addItem("1")
            .addItem("2")
            .addItem("3")
            .addItem("4")
            .addItem("5")
            .addItem("6")
            .addItem("7")
            .setDescription("Robot Maintenance Menu - Motor/Servo Menu")
            .build()

        // Build the stick select menu
        stickSelectMenu = Menu.MenuBuilder()
            .addItem("Left Stick")
            .addItem("Right Stick")
            .setDescription("Robot Maintenance Menu - Select Stick")
            .build()
    }

    override fun run() {
        AsyncQuestionExecutor.askC1(rootMenu) { item: Item ->
            while (true)
                when (item.name) {
                    "Test Motors/Servos" -> {
                        AsyncQuestionExecutor.askC1(motorMenu) { item: Item ->
                            // Convert the item name to an integer
                            val motorIndex = item.name.toInt()
                            // Get the motor
                            val motor = when (motorIndex) {
                                0 -> Devices.motor0
                                1 -> Devices.motor1
                                2 -> Devices.motor2
                                3 -> Devices.motor3
                                4 -> Devices.motor4
                                5 -> Devices.motor5
                                6 -> Devices.motor6
                                7 -> Devices.motor7
                                else -> throw IllegalArgumentException("Invalid motor index")
                            }
                            while (true)
                                // Ask the user to select a stick
                                AsyncQuestionExecutor.askC1(stickSelectMenu) { item: Item ->
                                    when (item.name) {
                                        "Left Stick" -> {
                                            leftStick.add(motor)
                                        }

                                        "Right Stick" -> {
                                            rightStick.add(motor)
                                        }
                                    }
                                    return@askC1;
                                }
                        }
                    }

                    "Test Sensors" -> {
                        // TODO: Add sensor testing
                    }

                    "Test Vision" -> {
                        // TODO: Add vision testing
                    }

                    "View Active Mappings" -> {
                        val activeMappingMenu = Menu.MenuBuilder()
                            .addItem("Unbind All Left Stick")
                            .addItem("Unbind All Right Stick")
                            .addItem("Return")
                            .setDescription(
                                "Robot Maintenance Menu - Active Mappings\nLeft Stick: ${
                                    leftStick.joinToString(
                                        ", "
                                    )
                                }\nRight Stick: ${rightStick.joinToString(", ")}"
                            )
                            .build()
                        while (true)
                            AsyncQuestionExecutor.askC1(activeMappingMenu) { item: Item ->
                                when (item.name) {
                                    "Unbind All Left Stick" -> {
                                        leftStick.clear()
                                    }

                                    "Unbind All Right Stick" -> {
                                        rightStick.clear()
                                    }

                                    "Return" -> {
                                        // Do nothing
                                    }
                                }
                                return@askC1;
                            }
                    }
                }
        }
    }
}