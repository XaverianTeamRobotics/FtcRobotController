package org.firstinspires.ftc.teamcode.internals.base

abstract class Script {
    val thread: Thread = Thread { run() }

    abstract fun init()
    abstract fun run()
    abstract fun onStop()
}