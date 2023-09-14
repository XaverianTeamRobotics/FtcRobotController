package org.firstinspires.ftc.teamcode.internals.bindings;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Button {
    public Method getter;
    public Gamepad controller;

    protected Button(Gamepad controller, Method getter) {
        this.getter = getter;
        this.controller = controller;
    }

    public boolean getValue() {
        try {
            return (boolean) getter.invoke(controller);
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
