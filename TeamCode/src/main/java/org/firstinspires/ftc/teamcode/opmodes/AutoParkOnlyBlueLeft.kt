package org.firstinspires.ftc.teamcode.opmodes

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.time.Clock
import org.firstinspires.ftc.teamcode.internals.time.Timer
import java.util.*

class AutoParkOnlyBlueLeft : OperationMode(), AutonomousOperation {
    var timer: Timer? = null
    var runner: AutoRunner? = null

    override fun construct() {
        timer = Clock.make(UUID.randomUUID().toString())
        val start = Pose2d(12.0, 64.50, Math.toRadians(-90.00))
        val auto = Auto(start)

            .begin()
            .lineTo(Vector2d(60.00, 64.50))
            .completeTrajectory()
            .complete()
        runner = AutoRunner(auto, auto.drivetrain, null, null, null)
    }

    override fun run() {
        runner!!.run()
    }

    override fun getNext(): Class<out OperationMode> {
        return LasagnaBot::class.java
    }
}
