package org.firstinspires.ftc.teamcode.internals.bindings;

import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Axis {
    public Method getter;
    public Gamepad controller;

    protected Axis(Gamepad controller, Method getter) {
        this.getter = getter;
        this.controller = controller;
    }

    public double getValue() {
        try {
            return (double) getter.invoke(controller);
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
