---
title: Motors
---
# Motors

Motors are stored in the `Devices` class.

There are 8 different motors:
 - `Devices.motor0`
 - `Devices.motor1`
 - `Devices.motor2`
 - `Devices.motor3`
 - `Devices.motor4`
 - `Devices.motor5`
 - `Devices.motor6`
 - `Devices.motor7`

:::caution
To use motors 4-7 you must connect a REV Expansion Hub.
:::

## Usage
 - `setPower(double power)` - Sets the power of the motor. Accepts values between -100 and 100.
 - `setSpeed(double speed)` - Sets the speed of the motor. Accepts values between -100 and 100.This uses a PID loop to set a constant robot speed.

