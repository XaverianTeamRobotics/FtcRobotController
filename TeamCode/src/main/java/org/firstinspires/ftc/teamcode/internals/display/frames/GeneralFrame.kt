package org.firstinspires.ftc.teamcode.internals.display.frames

import org.firstinspires.ftc.teamcode.internals.display.Frame
import java.util.concurrent.CopyOnWriteArrayList

class GeneralFrame: Frame {
    private val lines = CopyOnWriteArrayList<String>()
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
        if (!frozen) display()
    }

    fun addLine(index: Int, line: String) {
        lines.add(index, line)
        if (!frozen) display()
    }

    fun removeLine(index: Int) {
        lines.removeAt(index)
        if (!frozen) display()
    }

    fun removeLine(line: String) {
        lines.remove(line)
        if (!frozen) display()
    }

    fun clear() {
        lines.clear()
        if (!frozen) display()
    }

    fun addLines(vararg lines: String) {
        this.lines.addAll(lines)
        if (!frozen) display()
    }

    fun freeze() {
        frozen = true
    }

    fun unfreeze() {
        frozen = false
    }
}