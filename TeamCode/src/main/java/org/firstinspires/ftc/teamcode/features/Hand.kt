package org.firstinspires.ftc.teamcode.features

import com.acmerobotics.roadrunner.util.NanoClock
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import java.util.*

class Hand() : Feature(), Buildable {

    private var auto = false

    constructor(auto: Boolean) : this() {
        this.auto = auto
    }

    override fun build() {
        if(!auto) {
            Devices.servo1.position = homePosLeft
            Devices.servo0.position = homePosRight
        }else{
            manualClose()
        }
    }

    override fun loop() {
        /*
        Pressing x on either controller releases the hand
         */
        if(!auto) {
            if ((open && Devices.controller1.y)) {
                manualClose()
            } else if (!open && Devices.controller1.a) {
                manualOpen()
            }
        }
    }

    companion object {
        // inits
        private var timer: Timer = Clock.make(UUID.randomUUID().toString())
        private var open = true
        private var second = 0.0
        private const val homePosLeft = 67.5
        private const val homePosRight = 37.5
        @JvmStatic
        fun autoOpen() {
            timer.reset()
            manualOpen()
        }
        @JvmStatic
        fun autoClose() {
            timer.reset()
            manualClose()
        }
        @JvmStatic
        fun complete(): Boolean {
            return timer.elapsed(0.2)
        }
        @JvmStatic
        fun manualOpen() {
            Devices.servo1.position = homePosLeft
            Devices.servo0.position = homePosRight
            open = true
            second = NanoClock.system().seconds() + 2
        }
        @JvmStatic
        fun manualClose() {
            Devices.servo1.position = homePosLeft + 7.5
            Devices.servo0.position = homePosRight - 7.5
            open = false
        }
    }
}