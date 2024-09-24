package org.firstinspires.ftc.teamcode.internals.base

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

abstract class BaseOpMode : LinearOpMode() {
    private val subThreads: List<Thread>
        get() = scripts.map { it.thread }
    private val allThreads: List<Thread>
        get() = listOf(mainThread) + subThreads
    private val mainThread: Thread = Thread { run() }

    override fun runOpMode() {
        HardwareManager.init(hardwareMap, gamepad1, gamepad2, telemetry, HardwareSecret.secret)
        construct()
        waitForStart()
        started = true
        for (thread in allThreads) {
            thread.start()
        }
        while (opModeIsActive()) {
            for (script in scripts) {
                if (!script.thread.isAlive) {
                    scripts.remove(script)
                }
            }
            sleep(50)
        }
        allThreads.forEach { it.interrupt() }
        onStop()
        allThreads.forEach { it.join() }
        scripts.forEach { it.onStop() }
    }

    abstract fun construct()
    abstract fun run()
    abstract fun onStop()

    companion object {
        const val DRIVETRAIN_GROUP_NAME = "Drivetrain"
        const val DEBUG_GROUP_NAME = "Debug"
        const val AUTONOMOUS_GROUP_NAME = "Autonomous"
        const val FULL_GROUP_NAME = "Full"

        @Synchronized fun addScript(script: Script) {
            script.init()
            if (started) {
                script.thread.start()
            }
            scripts.add(script)
        }
        private val scripts: MutableList<Script> = mutableListOf()
        private var started = false
    }
}