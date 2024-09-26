---
title: Handling Input
slug: input
---

# Handling Input

Our programs are given input by the gamepads. There are two gamepads which are both accessible
from the `HardwareManager` class, named `gamepad1` and `gamepad2`.

## Connection Gamepads to the Driver Station

> [!NOTE]
> If you have an Android phone running a version later than Android 7.0, you can use your own
> device as a driver station. However, only approved devices can be used in a competition.

To connect a gamepad to a driver station, you will first need to find a suitable USB cable. Most 
controllers have a USB-A connector on the end, so you will need a USB-A to Micro-USB adapter to
connect it to the driver station.

Once the controller is connected, you need to specify which controller should be used as `gamepad1`
and which should be used as `gamepad2`. You do this by holding the `OPTIONS` button on the controller
(`MENU` button on the Xbox controller) and pressing the `A` button on the controller you want to use
as `gamepad1`. The controller you want to use as `gamepad2` should be pressed with the `B` button.

An indicator light on the gamepads will show which controller is `gamepad1` and which is `gamepad2`.
Blue light indicates `gamepad1` and red light indicates `gamepad2`. _Some gamepads do not have a light
indicator._

## Using the Controllers

The gamepads have a variety of buttons and triggers that can be used to control the robot. The gamepad
buttons are accessed through the `gamepad1` and `gamepad2` objects in the `HardwareManager` class.
Each gamepad has properties corresponding to the buttons and axes on the controller.
Buttons give values of `true` or `false` depending on whether they are pressed or not, triggers give
values between `0` and `1`, and joysticks give values between `-1` and `1` in both the x and y directions.

### Examples

```kotlin
gamepad1.a // Boolean value for the A button
gamepad1.left_stick_x // Float value for the x-axis of the left joystick
gamepad2.right_trigger // Float value for the right trigger
```

## Common Programming Techniques

> [!TIP]
> Whenever you use a hardware device, it slightly slows down other processes. It is important to only
> use the hardware when necessary. These techniques will help with this.

### Storing the Previous State

A common programming technique is to store the previous state of the gamepad and compare it to the
current state. This is useful for detecting when a button is pressed or released, or when a joystick
is moved. We do this by copying the current state of the gamepad to a variable at the end of the loop.

```kotlin
fun run() {
    val previousState = Gamepad()
    while (true) {
        // ...
        // Your code here
        // ...

        // Store the current state of the gamepad
        previousState.copy(gamepad1)
    }
}
```

You can use the `previousState` variable to compare the current state of the gamepad to the previous
state.

### Rising Edge Detection

A common programming technique is to determine when a button is pressed, but not perform the action
again until the button is released and pressed again. This is known as rising edge detection. It makes
use of storing a previous state of the gamepad and comparing it to the current state.

```kotlin
if (gamepad1.a && !previousState.a) {
    // The A button was just pressed
}
```

### Falling Edge Detection

Falling edge detection is the opposite of rising edge detection. It determines when a button is released,
but not perform the action again until the button is pressed and released again.

```kotlin
if (!gamepad1.a && previousState.a) {
    // The A button was just released
}
```

### Axis Change Detection

Being able to detect when an axis value changes is useful for optimizing code. Often, it is not necessary
to perform an action all the time, and sometimes we only want to perform an action when the joystick is
moved. This can be done by comparing the current state of the joystick to the previous state.

```kotlin
if (gamepad1.left_stick_x != previousState.left_stick_x) {
    // The x-axis of the left joystick has changed
}
```

### Toggles

A toggle is a button which makes an action occur until it is pressed again. This uses a boolean variable to
keep track of the state of the toggle and changes it when the button is pressed. It uses a rising edge detector
to only change the state when the button is pressed, not when it is held.

```kotlin
var toggleState = false // This must go outside the while loop
// ...
if (gamepad1.a && !previousState.a) {
    toggleState = !toggleState // Change the toggleState variable to the opposite of what it was
    
    if (toggleState) {
        // The toggle is true
    } else {
        // The toggle is false
    }
}
```

## Gamepad Feedback

> [!WARNING]
> Gamepad feedback is not available on all gamepads. The Logitech F310 gamepad does not have any feedback,
> while the Xbox 360 gamepad has vibration feedback and not LED feedback.

Gamepads have two ways of providing feedback: vibration and LED lights.

### Vibration Feedback

Simple vibration feedback is a simple on/off vibration. It can be used to provide feedback to the driver
when a certain action is performed. It is used with the `rumble()` function of the gamepad. It takes in
one argument, the duration of the vibration in milliseconds.

```kotlin
gamepad1.rumble(1000) // Vibrate for 1 second
```

There is also the `rumbleBlips()` function which takes in one argument: the number of blips. A blip is a
short vibration. The function will vibrate for the duration of the blip, then pause for the same duration,
and repeat for the number of blips specified.

```kotlin
gamepad1.rumbleBlips(3) // Vibrate 3 times
```

### LED Feedback

The LED on the gamepad is controlled by the `setLedColor()` function. It takes in four arguments:
the red component, the green component, the blue component, and the duration in milliseconds. The
color components are between `0.0` and `1.0`.

```kotlin
gamepad1.setLedColor(1.0, 0.0, 0.0, 1000) // Set the LED to red for 1 second
```

If you want the LED to stay on indefinitely, you can set the duration to a special constant.

```kotlin
gamepad1.setLedColor(0.0, 1.0, 0.0, Gamepad.LED_DURATION_CONTINUOUS) // Set the LED to green indefinitely
```

> [!WARNING]
> The Driver Station uses LEDs to specify which gamepad is `gamepad1` and which is `gamepad2`. A green
> LED indicates no assignment, blue indicates `gamepad1`, and red indicates `gamepad2`. If your code
> sets the LED to any of those colors, it may confuse the user into thinking the gamepad is not connected
> properly. For that reason, avoid manually setting the LED to red, green, or blue.