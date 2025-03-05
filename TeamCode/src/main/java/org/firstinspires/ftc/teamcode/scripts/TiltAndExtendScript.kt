package org.firstinspires.ftc.teamcode.scripts

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.internals.display.Display
import org.firstinspires.ftc.teamcode.internals.display.frames.FrameOfFrames
import org.firstinspires.ftc.teamcode.internals.display.frames.GeneralFrame
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.gamepad2
import org.firstinspires.ftc.teamcode.internals.templates.Script
import java.lang.Thread.sleep
import kotlin.math.abs

class TiltAndExtendScript(private val tiltMaxVel: Double, private val extendMaxVel: Double, private val extend2MaxVel: Double,
                          private val pMultiplier: Double = 1.0,
                          var gamepadEnable: Boolean = true
): Script() {
    private val tiltMotor = HardwareManager.motors.get("tilt", 5) as DcMotorEx
    private val extendMotor = HardwareManager.motors.get("extend", 4) as DcMotorEx
    private val extend2Motor = HardwareManager.motors.get("extend2", 7) as DcMotorEx
    private val frame = GeneralFrame()

    var mode = Mode.TARGET
    var tiltTarget: Int get() = tiltMotor.currentPosition; set(value) {tiltMotor.targetPosition = value}
    var extendTarget: Int get() = extendMotor.currentPosition; set(value) {extendMotor.targetPosition = value}
    var extend2Target: Int get() = extend2Motor.currentPosition; set(value) {extend2Motor.targetPosition = value}
    
    var tiltSpeed: Double get() = tiltMotor.velocity; set(value) {tiltMotor.velocity = value}
    var extendSpeed: Double get() = extendMotor.velocity; set(value) {extendMotor.velocity = value}
    var extend2Speed: Double get() = extend2Motor.velocity; set(value) {extend2Motor.velocity = value}

    override fun init() {
        instance = this
        Display.addFrame("TaE", frame)
        initMotor(tiltMotor, tiltMaxVel)
        initMotor(extendMotor, extendMaxVel)
        initMotor(extend2Motor, extend2MaxVel)
        tiltTarget = -350
    }

    fun initMotor(motor: DcMotorEx, maxVel: Double) {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.targetPosition = 0
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.targetPositionTolerance = 1
        val k = motor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION)
        k.p *= pMultiplier // higher = stiffer, lower = more efficient
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, k)
        switchToTargetMode(motor, maxVel)
    }
    
    private fun switchToTargetMode(motor: DcMotorEx, maxVel: Double) {
        motor.velocity = maxVel
        motor.power = 1.0
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }
    
    fun switchToTargetMode() {
        mode = Mode.TARGET
        switchToTargetMode(tiltMotor, tiltMaxVel)
        switchToTargetMode(extendMotor, extendMaxVel)
        switchToTargetMode(extend2Motor, extend2MaxVel)
    }
    
    private fun switchToSpeedMode(motor: DcMotorEx) {
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.power = 1.0
        motor.velocity = 0.0
    }
    
    fun switchToSpeedMode() {
        mode = Mode.SPEED
        switchToSpeedMode(tiltMotor)
        switchToSpeedMode(extendMotor)
        switchToSpeedMode(extend2Motor)
    }
    
    fun switch(mode: Mode) {
        when (mode) {
            Mode.SPEED -> switchToSpeedMode()
            Mode.TARGET -> switchToTargetMode()
        }
    }

    override fun run() {
        while (scriptIsActive()) {
            frame.frozen = true
            frame.clear()
            frame.addLine("Mode: $mode")
            frame.addLine("Gamepad: ${if (gamepadEnable) "Enabled" else "Disabled"}")

            if (gamepadEnable) {
                if (mode == Mode.SPEED) {
                    if (gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y) {
                        switch(Mode.TARGET)
                    }
                } else if (mode == Mode.TARGET) {
//                    if (abs(gamepad2.right_stick_x) > 0.2 || abs(gamepad2.right_stick_y) > 0.2) {
//                        switch(Mode.SPEED)
//                    }
                }

                if (mode == Mode.SPEED) {
                    tiltSpeed = gamepad2.right_stick_y.toDouble() * tiltMaxVel
                    extendSpeed = gamepad2.right_stick_x.toDouble() * extendMaxVel
                } else if (mode == Mode.TARGET) {
                    if (gamepad2.a) {
                        tiltTarget = 0
                        extendTarget = 0
                        extend2Target = 0
                    } else if (gamepad2.b) { // pickup
                        tiltTarget = -33
                        extendTarget = -41
                        extend2Target = -313
                    } else if (gamepad2.x) { // hb
                        extendTarget = -356
                        extend2Target = -1291
                        sleep(500)
                        tiltTarget = -975
                    } else if (gamepad2.y) { // retract
                        tiltTarget = -350
                        extendTarget = 0
                        extend2Target = 0
                    } else if (gamepad2.dpad_up) { // sub
                        tiltTarget = -21
                        extendTarget = 133
                        extend2Target = -1050
                    } else if (gamepad2.dpad_down) { // lb
                        tiltTarget = -743
                        extendTarget = -464
                        extend2Target = -468
                    }
                }
            }

            when (mode) {
                Mode.SPEED -> {
                    frame.addLine("Tilt: $tiltSpeed")
                    frame.addLine("Extend: $extendSpeed")
                    frame.addLine("Extend2: $extend2Speed")
                }
                Mode.TARGET -> {
                    frame.addLine("Tilt: $tiltTarget")
                    frame.addLine("Extend: $extendTarget")
                    frame.addLine("Extend2: $extend2Target")
                }
            }

            frame.addLine("\n")
            frame.addLine("Tilt Current: ${tiltMotor.getCurrent(CurrentUnit.AMPS)}")
            frame.addLine("Extend Current: ${extendMotor.getCurrent(CurrentUnit.AMPS)}")
            frame.addLine("Extend2 Current: ${extend2Motor.getCurrent(CurrentUnit.AMPS)}")
            frame.frozen = false
        }
    }

    override fun onStop() {

    }
    
    enum class Mode {
        TARGET, SPEED
    }
    
    companion object {
        var instance: TiltAndExtendScript? = null
            private set
    }
}