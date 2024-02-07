package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

/**
 * <blockquote>
 * No. We are not naming them the I and J cameras. No one will understand.<br></br>
 * <cite> Braden</cite>
</blockquote> *
 */
class JCam : Feature(), Buildable {
    override fun build() {
        Devices.servo2.position = 100.0
        time = System.currentTimeMillis()
    }

    override fun loop() {
        if (down) {
            Devices.servo2.position = 0.0
        } else {
            Devices.servo2.position = 100.0
        }
    }

    companion object {
        private var down = false

        private var time: Long = 0

        @JvmStatic
        fun toggle() {
            down = !down
            time = System.currentTimeMillis() + 1000
        }

        @JvmStatic
        fun complete(): Boolean {
            return System.currentTimeMillis() > time
        }

        @JvmStatic
        fun down(): Boolean {
            return down
        }
    }
}
