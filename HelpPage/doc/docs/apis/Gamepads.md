---
title: Gamepads
---
# Gamepads

Gamepads are stored in the `Devices` class.

There are two:
 - `Devices.controller0`
 - `Devices.controller1`

## Usage

To use the gamepads, use the method named `get` + the name of the button or stick.
Axes return `double` and buttons return `boolean`.

## Button Registry

First you must enable the feature, so add this to the `construct()` function of the `OperationMode`:
```java
Devices.enableButtonRegistration();
```

To register a button, use the method `registerButton` in the `Devices` class.

```java
Devices.controller#.registerButton(GamepadRequestInput.Button , "Button Usage")
```

If a button isn't registered, or you attempt to register a button twice the opmode will fail.
To search for a button use the method `searchForButton` in the `Devices` class.

```java
Devices.controller#.buttonSearch("Button Usage")
```

See the Buttoner.java Opmode to see sample code

If you want a list of buttons and their uses, possibly to print to the driver station, use the method `getButtonRegistry` in the `Devices` class.

```java
Devices.genList();
```