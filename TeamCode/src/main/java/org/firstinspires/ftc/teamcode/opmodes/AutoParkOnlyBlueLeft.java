package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;

import java.util.UUID;

public class AutoParkOnlyBlueLeft extends OperationMode implements AutonomousOperation {

    Timer time;
    AutoRunner runner;

    @Override
    public void construct() {
        time = Clock.make(UUID.randomUUID().toString());
        Pose2d start = new Pose2d(12, 64.50, Math.toRadians(-90.00));
        Auto auto = new Auto(start)

            .begin()
                .lineTo(new Vector2d(60.00, 64.50))
            .completeTrajectory()
            .complete();
        runner = new AutoRunner(auto, auto.getDrivetrain(), null, null, null);
    }

    @Override
    public void run() {
        runner.run();
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return SmallbotProduction.class;
    }

}
