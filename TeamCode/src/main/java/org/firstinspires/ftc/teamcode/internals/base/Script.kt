package org.firstinspires.ftc.teamcode.internals.base

abstract class Script {
    val thread: Thread = Thread { run() }

    fun scriptIsActive(): Boolean = !thread.isInterrupted

    abstract fun init()
    abstract fun run()
    abstract fun onStop()
}