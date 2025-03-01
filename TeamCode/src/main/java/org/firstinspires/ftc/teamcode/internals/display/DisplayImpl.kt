package org.firstinspires.ftc.teamcode.internals.display

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
        frames.values.forEach { println(it.display()) }
    }

    override fun clear() {
        frames.clear()
    }
}