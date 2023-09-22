---
title: Project Structure
sidebar_position: 1
---

# Project Structure

## The main modules

There are two main modules
 - **FtcRobotController** - This is the main robot code. It contains the backend code.
 - **TeamCode** - This is where we put all of our code. All modifications should be in this module.

## FtcRobotController

:::danger
`FtcRobotController` is the module which should be left alone. Any small change will have unintended consquences.
:::

This code is provided by the FIRST programmers to interface with the hardware.

## TeamCode

This is where we put all of our code. All modifications should be in this module.
It then has three different folders inside of it.
 - **features** - This is where we put all of our code that is not robot specific. This includes things like
our drivetrain, linear slides, or grabbers.
 - **opmodes** - This is where we put all of our code that is robot specific. These normally consist of compilations of features.
More detail will be discused later
 - **internals** - This is where we put all of our code that is not meant to be used by the user. This includes things like
telemetry, hardware interfaces, and other backend code. Any changes here are strongly discouraged.