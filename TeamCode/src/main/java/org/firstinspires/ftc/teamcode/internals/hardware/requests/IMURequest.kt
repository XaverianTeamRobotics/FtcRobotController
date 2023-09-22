package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.internals.misc.Vector3
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.IMUData

class IMURequest(name: String) : ScriptParameters.Request(name) {
    val imu: BNO055IMU

    init {
        imu = HardwareGetter.hardwareMap!!.get(BNO055IMU::class.java, name)
        val parameters = BNO055IMU.Parameters()
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.loggingEnabled = false
        parameters.loggingTag = "IMU"
        parameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters)
    }

    override fun issueRequest(p0: Any?): Any {
        return IMUData(
            Vector3(imu.angularOrientation.firstAngle.toDouble(), imu.angularOrientation.secondAngle.toDouble(),    // Orientation
                imu.angularOrientation.thirdAngle.toDouble()                                                        // Orientation
            ),
            Vector3(imu.linearAcceleration.xAccel, imu.linearAcceleration.yAccel, imu.linearAcceleration.zAccel),                     // Acceleration
        )
    }

    override fun getOutputType(): Class<*> {
        return IMUData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}