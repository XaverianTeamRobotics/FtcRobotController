package org.firstinspires.ftc.teamcode.internals.display.frames

import org.firstinspires.ftc.teamcode.internals.display.Frame

class GeneralFrame: Frame {
    private val lines = mutableListOf<String>()
    private var contentBeforeFreeze = ""
    var frozen = false
        set(value) {
            if (value) {
                contentBeforeFreeze = display()
            }
            field = value
        }

    override fun display(): String =    if (frozen) contentBeforeFreeze
                                        else lines.joinToString("\n")

    fun addLine(line: String) {
        lines.add(line)
    }

    fun addLine(index: Int, line: String) {
        lines.add(index, line)
    }

    fun removeLine(index: Int) {
        lines.removeAt(index)
    }

    fun removeLine(line: String) {
        lines.remove(line)
    }

    fun clear() {
        lines.clear()
    }

    fun addLines(vararg lines: String) {
        this.lines.addAll(lines)
    }

    fun freeze() {
        frozen = true
    }

    fun unfreeze() {
        frozen = false
    }
}