package org.firstinspires.ftc.teamcode.internals.display.frames

import org.firstinspires.ftc.teamcode.internals.display.Frame

class FrameOfFrames: Frame {
    private val frames = mutableListOf<Frame>()

    override fun display(): String {
        val sb = StringBuilder()
        frames.forEach {sb.append(it.display())}
        return sb.toString()
    }

    fun addFrame(frame: Frame) {
        frames.add(frame)
    }

    fun removeFrame(frame: Frame) {
        frames.remove(frame)
    }

    fun clear() {
        frames.clear()
    }
}