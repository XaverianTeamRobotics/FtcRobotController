package org.firstinspires.ftc.teamcode.internals.display.frames

import org.firstinspires.ftc.teamcode.internals.display.Frame

class ActionFrame(private val action: () -> String): Frame {
    override fun display(): String = action()
}