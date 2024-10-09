package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.gamepad1
import org.firstinspires.ftc.teamcode.internals.base.HardwareManager.motors
import org.firstinspires.ftc.teamcode.internals.base.Script

class ContinuousMotorScript(private val id: Int = 0, private val inverted: Boolean = false,
                            private val input: () -> Double = { (gamepad1.right_trigger - gamepad1.left_trigger).toDouble() }) : Script() {
    private val motor = motors.get("cm$id", 0)

    override fun init() {

    }

    override fun run() {
        var prev: Double = 0.0
        while (true) {
            val i = (if (inverted) -1.0 else 1.0) * input()
            if (i != prev) {
                motor.power = i
                RobotLog.v("CMS${id}: Set power to ${i}")
            }
            prev = i
        }
    }

    override fun onStop() {

    }

    companion object {
        fun threeWayInput(pos: () -> Boolean, neg: () -> Boolean, stop: () -> Boolean): () -> Double {
            var state = 0.0
            return {
                if (pos()) {
                    state = 1.0
                } else if (neg()) {
                    state = -1.0
                } else if (stop()) {
                    state = 0.0
                }
                state
            }
        }

        fun twoButtonInbut(pos: () -> Boolean, neg: () -> Boolean, pow: Double = 1.0): () -> Double {
            return {
                if (pos()) pow
                else if (neg()) -pow
                else 0.0
            }
        }
    }
}