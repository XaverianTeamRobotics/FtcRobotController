package org.firstinspires.ftc.teamcode.internals.display

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager

class DisplayImpl @Throws(IllegalStateException::class) constructor(): Display  {
    init {
        if (Display.instance != null) {
            throw IllegalStateException("DisplayImpl already created")
        }
        Display.instance = this
    }

    private val frames = mutableMapOf<String, Frame>()

    override fun addFrame(name: String, frame: Frame) {
        frames[name] = frame
    }

    override fun removeFrame(name: String) {
        frames.remove(name)
    }

    override fun getFrame(name: String): Frame {
        return frames[name]!!
    }

    override fun display() {
        frames.values.forEach { HardwareManager.telemetry.addLine(it.display()) }
        HardwareManager.telemetry.update()
    }

    override fun clear() {
        frames.clear()
    }
}