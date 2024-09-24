---
title: Welcome
---

# Welcome

## About us

Our robot uses code made completely in Kotlin, a fairly new language
made by Google. We use a standard set of libraries provided by FIRST
that control the hardware of the robot, in addition to many 
community-made libraries to enhance the process. If you want to
learn Kotlin, we recommend you check out the following articles
from the Kotlin website:
 - [Basic syntax](https://kotlinlang.org/docs/basic-syntax.html)
 - [Classes](https://kotlinlang.org/docs/classes.html)
 - [Functions](https://kotlinlang.org/docs/functions.html)
 - [Collections](https://kotlinlang.org/docs/collections-overview.html)

We store all of our code on GitHub. Our GitHub URL can be found by
pressing the GitHub icon at the top right of the page.

## Program Structure

### OpModes and Scripts

The code uses opmodes to control the robot. An opmode is a class that
contains any code the user wants. Users are presented with a list of
opmodes and choose which one they want to run. 

Opmodes begin with the `init()` method. The `init()` method is called once
when the user presses the **INIT** button on the Driver Station.

Once the user presses the Play button, the `run()` function runs. The `run()`
function is run once, and code is required to be placed in a while loop
to run continuously.

The last function that runs is the `onStop()` method. This method is called
when the user presses the Stop button on the Driver Station, but is also
called if there is a timer on the driver station that reaches zero.

We also have other components called scripts. Scripts work similarly to opmodes,
but are not controlled by the driver station, instead they are added in the 
`init()` function of an opmode. Multiple scripts can be added to an opmode,
and scripts are reusable between opmodes.

We will touch more in-depth on opmodes and scripts later.

### Hardware

Hardware is controlled by the `HardwareManager` class. It
contains all the hardware objects that are available to the robot, which should 
be configured in the Driver Station.