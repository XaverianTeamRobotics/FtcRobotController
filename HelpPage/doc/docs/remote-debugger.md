---
title: Remote Debugger
sidebar_label: Remote Debugger
pagination_prev: null
pagination_next: null
---

# Remote Debugger
The Remote Debugger allows you to manually control and view motors from your computer. It is designed to be used by the
Hardware Team to test their new prototypes without having to write a custom OpMode.

The Debugger exists in two parts, the `FTC Remote Debugger` app and the `Debugger` feature. They communicate via a WebSocket
connection.

## Installing the Client
The source code for the `FTCRemoteDebugger` can be found in the [GitHub repository](https://github.com/XaverianTeamRobotics/FtcRemoteDebugger).
You can either run it from source code using [`npm`](#running-from-source), [build your own binaries](#building-from-source), or 
[use the pre-built binaries](https://github.com/XaverianTeamRobotics/FtcRemoteDebugger/releases/latest).

:::caution
While the application can be run on any platform, we currently only host pre-built binaries for Windows.
:::

### Running from Source
To run the source code, run the command `npm i` to install dependencies, then `npm start` to start the application.

### Building from Source
To build the source code, run `npm i` to install dependencies, then `npm package`. The output will be stored in the `out`
directory, formatted for whatever your current OS is.

## Connecting to the Robot
To connect to the robot, the robot must have a currently running supported OpMode. Currently, only OpModes with the feature
explicitly added are supported. To add the feature, add the following line to your OpMode's `init()` method:
```java
registerFeature(new Debugger());
```
There is also a special OpMode, `RemoteDebuggerOpMode`, which is designed to be used with the Remote Debugger.

Your computer also needs to be connected to the same network as the robot.
Once both conditions are met, you can connect to the robot by pressing the **_Connect to Robot_** button.

## Manipulating Motors
The Remote Debugger allows you to manipulate motors in real time. To use the motors, just type the desired power level from
-100 to 100 into the text box and press the **_Set Motor (number) Power_** button. The power level will be sent to the robot, and the motor
will be set to that power level. You can also view the motor's current power level below the _Set_ buttons.

There is also a **_Set All Motor Powers_** button, which allows you to set all motors to the same power level.

If you have give a mis-input and the robot does some very bad things, there is a red **_o no_** button which will set all motors to 0.

:::note
Only motors marked as enabled by the OpMode will be allowed for use. The others will be grayed out and disabled. 
:::

:::caution
The view of the motors is on a scale from -1 to 1, but the input box is on the scale of -100 to 100.
:::

## For Developers: Adding Support for the Remote Debugger
Support for the Remote Debugger can be added to any OpMode by adding the following line to the `init()` method:
```java
registerFeature(new Debugger());
```
By default, no motors will be enabled. To enable a motor, use the following code:
```java
RDWebSocketServer.enableMotorStatic(0);
```
:::danger
If your OpMode persistently sets its own value for a motor, it will override the value set by the Remote Debugger.
:::
