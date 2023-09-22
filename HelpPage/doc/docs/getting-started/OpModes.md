---
title: OperationModes
sidebar_position: 2
---

# OperationModes

## What is an OperationMode?

Opmodes are the starting point of the code. There can be as many as one desires, and are selected from the driver station.
They are defined in the `TeamCode` module in the `opmodes` folder. They are defined as a class that extends `OperationMode` and either
implements `TeleOperation` or `AutonomousOperation`, depending on whether or not the code should be run in teleop or autonomous.

OperationModes consist of two main functions: `construct()` and `run()`. The `construct()` function is called once when the OperationMode is selected 
and the user pressed `Init` button the Driver Station. 

The `run()` function is called repeatedly once the user presses the `Play` button until the user presses the `Stop` button.

:::tip
You should **NOT** have a `while (true)` loop in your `run()` function. This will cause the code to behave strangely and
is not a recommended practice.
:::

## Example OperationMode

```java
public class MyOperationMode extends OperationMode implements TeleOperation {
    public void construct() {
        // This is called once when the user presses the Init button
    }

    public void run() {
        // This is called repeatedly when the user presses the Play button
    }
}
```