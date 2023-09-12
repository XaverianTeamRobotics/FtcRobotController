# The Emulator

The emulator consists of a few main parts:

  1. The `EmulatedOpMode`
  2. Emulated hardware
  3. The `HardwareGetter`

## Emulated Hardware

Emulated hardware behaves like normal hardware except it controlls no physical device and gets any required input 
from variables manually set.

Emulated hardware can be found in the `org.firstinspires.ftc.teamcode.hardware.emulated` package.

After adding your own emulated hardware, you should add it to the `HardwareGetter` with a function that retrieves an emulated device or a physical device.

## Emulated Op Modes
Emulated OpModes are your way to create something to be run on the emulator. They function like normal opModes, but should extend from the `EmulatedOpMode` class. 

The only major difference is where they should be placed. In order to be properly discovered, they must be placed in 
the tests folder. This folder has a green background in Android Studio.

In order to run an emulated OpMode, you dont run it like a normall OpMode.
Once all the code is made, to the left of the class name, a green arrow should appear. Clicking that will run the 
emulator.

### Special Methods

The `EmulatedOpMode` has a few methods that normal methods don't.

* `setTimeUntillAbort` and `getTimeUntilAbort` allow you to change the total amount of time untill the emulator 
  stops itself. The default is 30 seconds.
* `setGamepadValues` allows you to set a virtual gamepad's button values. They are given in `double` format, and for 
  any buttons, `1.0` represents `true`