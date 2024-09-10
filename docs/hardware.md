---
title: Using Hardware
slug: hardware
---

# {{ $frontmatter.title }}

> [!CAUTION]
> Site Under Construction

This guide will help you understand how to access and manage hardware components 
in your robot using the `HardwareManager` class.

Hardware devices are defined in the driver station and following this naming scheme:
`<deviceType><index>`
Examples:
- Motor 0: `motor0`
- Servo 1: `servo1`
- Distance Sensor 2: `distanceSensor2`
- Touch Sensor 3: `ts3`

## Accessing Motors

You can access motors using the `motors` property of `HardwareManager`.
Motors have the `power` property that can be set to control the motor's power.
Power values range from -1.0 to 1.0.

### Example

```kotlin
val leftMotor = HardwareManager.motors[0]
val rightMotor = HardwareManager.motors[1]

leftMotor.power = 0.5
rightMotor.power = -0.5
```

## Accessing Servos

You can access servos using the `servos` property of `HardwareManager`.
Servos have the `position` property that can be set to control the servo's position.
Position values range from 0.0 to 1.0.

### Example

```kotlin
val armServo = HardwareManager.servos[0]
armServo.position = 0.5
```

## Accessing Distance Sensors

You can access distance sensors using the `distanceSensor` property of `HardwareManager`.
Distance sensors have the `getDistance(unit)` method that returns the distance in the specified unit.

### Example

```kotlin
val frontSensor = HardwareManager.distanceSensor[0]
frontSensor.getDistance(DistanceUnit.CM)
```

## Accessing Touch Sensors

You can access touch sensors using the `touchSwitches` property of `HardwareManager`.
Touch sensors have the `isPressed` property that returns `true` if the sensor is pressed.

### Example

```kotlin
val touchSensor = HardwareManager.touchSwitches[0]
touchSensor.isPressed
```

## Using HardwareArray

The `HardwareArray` class provides methods to access hardware components by their index or name.

### Example

```kotlin
val motorByName = HardwareManager.motors["motorName"]
val motorByIndex = HardwareManager.motors[1]
```

## Error Handling

If you try to access a hardware component that does not exist, an `IllegalArgumentException` will be thrown. 
You can handle this exception as needed.

### Example

```kotlin
try {
    val motor = HardwareManager.motors[10]
} catch (e: IllegalArgumentException) {
    // Handle the error
}
```

## Accessing Named Hardware

To access named hardware devices using the `HardwareManager` class, you can use the `HardwareArray` class's `get` method with the device name as a parameter.

To use these, add `-NAME` to the end of the device name in the Driver Station configuration.

Examples would be:
- Motor 0 named `leftMotor`: `motor0-leftMotor`
- Servo 1 named `armServo`: `servo1-armServo`
- Distance Sensor 2 named `frontSensor`: `distanceSensor2-frontSensor`
- Touch Sensor 3 named `primary`: `ts3-primary`

### Example

```kotlin
val leftMotor = HardwareManager.motors["leftMotor"]
val armServo = HardwareManager.servos["armServo"]
val frontSensor = HardwareManager.distanceSensor["frontSensor"]
val touchSensor = HardwareManager.touchSwitches["primary"]
```

### Using Fallback

If you want to access a named hardware device with a fallback to an indexed device, 
you can use the `get` method with both the name and the fallback index.
This automatically accounts for if a device with that name is not found.

### Example

```kotlin
val leftMotor = HardwareManager.motors.get("leftMotor", 0)
val armServo = HardwareManager.servos.get("armServo", 0)
val frontSensor = HardwareManager.distanceSensor.get("frontSensor", 0)
val touchSensor = HardwareManager.touchSwitches.get("primary", 0)
```

This approach ensures that if the named device is not found, the method will return the device at the specified fallback index.