---
title: Servos
slug: servos
---

# Servos

_[Some information taken from REV Robotics Documentation](https://docs.revrobotics.com/rev-crossover-products/servo/srs-programmer/switching-operating-modes)_

Servos are a type of motor that, unlike motors, can be controlled to move to a specific position. The servos used for 
robotics are called "dual-mode" servos. These have the ability to rotate continuously like a motor, or move to a specific
angle like a servo. Unlike motors, they accept an input signal between 0 and 1, where 0 is the minimum position and 1 is the
maximum position. The center of the servo is at 0.5.

To use a servo, see [the Hardware page](/hardware#accessing-servos).

---

## Servo Programmers

The servo programmer (made by REV) can be used to modify properties of the servo, such as the minimum and maximum 
position and the mode.

![Servo Programmer](/servo_programmer.png)

To use the servo programmer, connect the servo to the top port of the programmer. The red wire corresponds to "+" and
the black wire corresponds to "-". The white wire corresponds to the signal wire. (Note that the servo cable might have
an orange signal wire).

### Switching servo modes

Follow the steps below to switch a servo between Continuous Mode and Servo Mode.

 1. Connect the servo to the programmer.
 2. Turn on the programmer.
 3. Slide the mode switch to the desired mode: C - Continuous, S - Servo.
 4. Press and release the PROGRAM button once.
 5. The PROGRAM LED should blink and then stay solid indicating success.

### Setting Angular Limits

Follow the steps below to set the angular limits for the Servo Mode.

 1. Start with the servo already configured in Servo Mode (see above section for more info)
 2. Connect the SRS to the programmer.
 3. Turn on the programmer.
 4. Slide the mode switch to S position.
 5. This step is optional, but recommended to make it easier to see the valid limit ranges. Please refer to the SRS User's Manual for more information about the valid limit ranges.
    1. Press and release the TEST button twice to enter Manual Test Mode (see Test Modes for more information). 
    2. Press the PROGRAM button to center the servo at 0°. 
    3. Press and release the TEST button once to leave the test mode.
 6. Manually rotate the servo to the desired left limit position.
 7. Press and release the LEFT button. The LEFT LED will illuminate if the position is valid.
 8. Manually rotate the servo to the desired right limit position.
 9. Press and release the RIGHT button. The RIGHT LED will illuminate if the position is valid. 
10. After both limits are set, press and release the PROGRAM button. The PROGRAM LED should blink and then stay solid indicating success.

The limits on the servo must be chosen carefully. The maximum rotation of the servo is 135 degrees in each direction.
The left or right limit also cannot be set to a value that is within 20 degrees of the center.

![Servo programmer limits](/sp_limits.avif)

---

## Using Servos in Continuous Mode

Using servos in continuous mode is similar to using motors. The only difference is that the power value is between 
0 and 1.0 instead of -1.0 and 1.0. A power of 0.5 will make the servo stop, a power of 0.0 will make the servo rotate backwards,
and a power of 1.0 will make the servo rotate forwards.

Because all our gamepad inputs are between -1.0 and 1.0, you will need to convert the input to the correct range.
We can express this with a simple math equation:

$$ f(x) = \dfrac{x + 1}{2} $$

Where $x$ is the input from the gamepad and $f(x)$ is the input to the servo.

In Kotlin, this can be written as:

```kotlin
val output = (input + 1) / 2
```

For more info, see [the Hardware page](/hardware#accessing-servos).