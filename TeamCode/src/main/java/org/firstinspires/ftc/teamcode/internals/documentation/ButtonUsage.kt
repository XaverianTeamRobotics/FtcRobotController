package org.firstinspires.ftc.teamcode.internals.documentation

@Repeatable
annotation class ButtonUsage(
    val description: String,
    val button: ButtonName,
    val controller: ControllerName
)

@Repeatable
annotation class ReferToButtonUsage(
    val referTo: String
)

annotation class ReferableButtonUsage(
    val referableAs: String
)

enum class ButtonName {
    A, B, X, Y,
    DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT,
    LEFT_BUMPER, RIGHT_BUMPER,
    LEFT_TRIGGER, RIGHT_TRIGGER,
    LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON,
    LEFT_STICK_X, LEFT_STICK_Y,
    RIGHT_STICK_X, RIGHT_STICK_Y,
    BACK, START,
}

enum class ControllerName {
    CONTROLLER_1, CONTROLLER_2,
}