---
title: Features
sidebar_position: 3
pagination_next: null
---

# Features

## What is a feature?

Features are reusable pices of code which can be recycled between robots. Think camera code, drivetrains, arms, etc.
Features are defined similarly to `OperationModes` in that they are defined as a class that extends `Feature`.
```java
public class MyFeature extends Feature {
    public void loop() {
        // This is called repeatedly when the user presses the Play button
    }
}
```
Optionally, the class can also implement `Buildable` to add the `build()` function. This is called once the user presses `Init`.
```java
public class MyFeature extends Feature implements Buildable {
    public void build() {
        // This is called once when the user presses the Init button
    }
    public void loop() {
        // This is called repeatedly when the user presses the Play button
    }
}
```

## How to add Features to an OperationMode
In the `OperationMode`, use the function `registerFeature()` to add a feature to the OperationMode. This function takes in a `Feature` object.
```
registerFeature(new MyFeature());
```
:::tip
Ideally this should be added to the `construct()` function of the `OperationMode`, but it can be added anywhere, although not recommded.
:::

