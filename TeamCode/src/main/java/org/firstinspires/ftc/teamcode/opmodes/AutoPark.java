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

public class AutoPark extends OperationMode implements AutonomousOperation {

    Timer time;
    int spot = 0;
    SleeveDetector sleeve;
    boolean findingSleeve = true;
    AutoRunner runner;

    @Override
    public void construct() {
        time = Clock.make(UUID.randomUUID().toString());
        Hand hand = new Hand(true);
        registerFeature(hand);
        FourMotorArm arm = new FourMotorArm();
        registerFeature(arm);
        sleeve = new SleeveDetector();
        registerFeature(sleeve);
        Pose2d start = new Pose2d(-35.84, 61.50, Math.toRadians(-90.00));
        Auto auto = new Auto(start)

            // FIRST CONE
            // its preloaded, we do nothing :D

            .begin()

            // FIRST PARK

            // when we're 2 inches into the path, raise the arm. we do ths 2 inches into the path to provide adequate clearance with the wall
            .addDisplacementMarker(2, () -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_LOW))
            // drive to spot 2
            .splineToConstantHeading(new Vector2d(-35.50, 43.93), Math.toRadians(270.65))
            .splineToConstantHeading(new Vector2d(-35.00, 11.70), Math.toRadians(270.45))
            .splineTo(new Vector2d(-38.01, 10.30), Math.toRadians(177.99)) // two
            .completeTrajectory()
            // once the arm reaches the correct height, park
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> {
                if(spot == 2) {
                    FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET);
                }
            })
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();

        Auto one = new Auto(auto.end())
            .begin()
            .lineTo(new Vector2d(-11.21, 8.28)) // one
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        Auto three = new Auto(auto.end())
            .begin()
            .turn(Math.toRadians(90.00))
            .lineTo(new Vector2d(-61.33, 11.27)) // three
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        runner = new AutoRunner(auto, auto.getDrivetrain(), one, null, three);
    }

    @Override
    public void run() {
        if(!findingSleeve) {
            runner.run();
        }else{
            if(findSleeve()) {
                runner.processSleeve(spot);
                findingSleeve = false;
            }
        }
    }

    private boolean findSleeve() {
        spot = sleeve.getSpot();
        return spot != 0;
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return SmallbotProduction.class;
    }

}
