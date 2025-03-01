package org.firstinspires.ftc.teamcode.scripts

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.huskylens
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareManager.telemetry
import org.firstinspires.ftc.teamcode.internals.templates.Script
import java.lang.Math.toDegrees
import kotlin.math.atan2


class HuskylensRotateScript(
): Script() {
    var servoPos = 0.0 //initial servo position


    override fun init() {

    }

    override fun run() {
        while (scriptIsActive()) {
            huskylens.arrows() //get the arrow data from the camera
            for (a in huskylens.arrows()) {  //for each arrow in the array of arrows

                var X0 = a.x_origin.toDouble() //x-coordinate of the tail of the arrow
                telemetry.addData("X0", X0)
                var Y0 = a.y_origin.toDouble() //y-coordinate of the tail of the arrow
                telemetry.addData("Y0", Y0)
                var XT = a.x_target.toDouble() //x-coordinate of the head of the arrow
                telemetry.addData("XT", XT)
                var YT = a.y_target.toDouble() //y-coordinate of the head of the arrow
                telemetry.addData("YT", YT)
                telemetry.update()
            }
        }

    }

    override fun onStop() {
    }


    companion object{

        fun getAngle(X0: Double, Y0: Double, XT: Double, YT: Double): Double {
            var angle = toDegrees(atan2(YT - Y0, XT - X0))
            telemetry.addData("angle", angle)
            var rawPos = angle / 90.0
            var servoPos = rawPos - 1
            telemetry.addData("servoPos", servoPos)
            telemetry.update()
            return servoPos
        }

    }
}