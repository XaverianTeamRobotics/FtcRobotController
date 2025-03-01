package org.firstinspires.ftc.teamcode.internals.display

interface Display {
    fun addFrame(name: String, frame: Frame)
    fun removeFrame(name: String)
    fun getFrame(name: String): Frame

    fun display()
    fun clear()

    companion object {
        var instance: Display? = null
            internal set

        fun addFrame(name: String, frame: Frame) {
            instance?.addFrame(name, frame)
        }

        fun removeFrame(name: String) {
            instance?.removeFrame(name)
        }

        fun getFrame(name: String): Frame? {
            return instance?.getFrame(name)
        }

        fun display() {
            instance?.display()
        }

        fun update() = display()

        fun clear() {
            instance?.clear()
        }

        fun create(): Boolean {
            if (instance != null) {
                clear()
                return false
            } else {
                instance = DisplayImpl()
                return true
            }
        }

        fun reset() {
            instance = null
            create()
        }
    }
}