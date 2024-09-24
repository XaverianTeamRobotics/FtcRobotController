---
title: OpModes
slug: opmodes
---

# {{ $frontmatter.title }}

OpModes in the context of the provided project are classes that control the robot's behavior. 
They are part of the robot control system and are used to define the actions the robot should perform during different phases of operation. 
Here's a detailed description of how OpModes work:

## Initialization (`init()` method)
The `init()` method is called once when the user presses the **INIT** button on the Driver Station.
This method is used to set up any necessary configurations or initial states before the robot starts running.

## Running (`run()` method)
Once the user presses the Play button, the `run()` method is executed.
The `run()` method is run once, and any continuous actions need to be placed inside a while loop to keep running as long as the OpMode is active.

## Stopping (`onStop()` method)
The `onStop()` method is called when the user presses the Stop button on the Driver Station.
It is also called if a timer on the Driver Station reaches zero.
This method is used to clean up or stop any ongoing actions safely.

## Scripts
Scripts are components that can be added to an OpMode.
They are not controlled by the Driver Station but are added in the `init()` function of an OpMode.
Multiple scripts can be added to an OpMode, and they are reusable between different OpModes.

## Hardware Management
The `HardwareManager` class is used to control the hardware objects available to the robot.
These hardware objects should be configured in the Driver Station.

## OpMode Annotations

OpMode annotations are used to register and categorize OpModes in the robot control system. These annotations provide metadata that helps the system identify and organize the OpModes. Here are the key annotations used:

### @TeleOp

The `@TeleOp` annotation is used to mark an OpMode as a teleoperated mode. This means the OpMode is intended to be run during the teleoperated period of a match, where the robot is controlled by drivers using gamepads.

**Example:**
```kotlin
@TeleOp(name = "Simple Mecanum OpMode", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class SimpleMecanumOpMode: BaseOpMode() {
    // OpMode implementation
}
```

### @Autonomous

The `@Autonomous` annotation is used to mark an OpMode as an autonomous mode. This means the OpMode is intended to be run during the autonomous period of a match, where the robot operates based on pre-programmed instructions without driver input.

**Example:**
```kotlin
@Autonomous(name = "Simple Autonomous OpMode", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class SimpleAutonomousOpMode: BaseOpMode() {
    // OpMode implementation
}
```

### Parameters

Both `@TeleOp` and `@Autonomous` annotations accept the following parameters:

- `name`: A string representing the name of the OpMode. This name will be displayed on the Driver Station.
- `group`: A string representing the group name of the OpMode. This is used to categorize OpModes into different groups for easier navigation on the Driver Station.

There are certain constants defined in the `BaseOpMode` class that can be used for group names:

- `BaseOpMode.DRIVETRAIN_GROUP_NAME`: Group name for drivetrain-related OpModes.
- `BaseOpMode.DEBUG_GROUP_NAME`: Group name for debug-related OpModes.
- `BaseOpMode.FULL_GROUP_NAME`: Group name for OpModes that represent a fully-featured robot.
- `BaseOpMode.AUTONOMOUS_GROUP_NAME`: Group name for autonomous-related OpModes.

# Example

Here is an example of a simple OpMode:

```kotlin
@TeleOp(name = "Simple Mecanum OpMode", group = BaseOpMode.DRIVETRAIN_GROUP_NAME)
class SimpleMecanumOpMode: BaseOpMode() {
    override fun construct() {
        addScript(MecanumDriveScript())
    }

    override fun run() {
        // Main loop code here
    }

    override fun onStop() {
        // Cleanup code here
    }
}
```

In this example:
- The `construct()` method adds a `MecanumDriveScript` to the OpMode.
- The `run()` method would contain the main loop code.
- The `onStop()` method would contain any cleanup code needed when the OpMode stops.

# How OpModes Work Internally

The multithreaded model of OpModes in the provided project allows for concurrent execution of the main 
robot control logic and additional scripts. When an OpMode starts, a hidden method called `runOpMode()` method initializes 
the hardware and calls the `construct()` method to set up any necessary configurations. After waiting for 
the start signal, it starts the main thread, which runs the `run()` method containing the primary control loop. 
Concurrently, any scripts added to the OpMode are also started in their own threads. This setup ensures that the 
main control logic and scripts can run simultaneously, allowing for more complex and responsive robot behavior.

During the execution of the OpMode, the system continuously checks if the OpMode is active and manages the 
lifecycle of the threads. If a script's thread terminates unexpectedly, it is removed from the list of 
active scripts. When the OpMode stops, either due to user intervention or a timer, all threads are 
interrupted and joined to ensure they terminate safely. The `onStop()` method is then called to perform 
any necessary cleanup. This multithreaded approach provides a robust framework for managing multiple concurrent 
tasks within an OpMode, enhancing the robot's ability to perform complex operations efficiently.