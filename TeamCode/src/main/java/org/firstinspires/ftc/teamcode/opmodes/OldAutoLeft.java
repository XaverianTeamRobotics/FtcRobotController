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

public class OldAutoLeft extends OperationMode implements AutonomousOperation {

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
        Pose2d start = new Pose2d(35.84, 61.50, Math.toRadians(-90.00));
        Auto auto = new Auto(start)

            // FIRST CONE
            // its preloaded, we do nothing :D

            .begin()

            // FIRST JNCT

            // when we're 2 inches into the path, raise the arm. we do ths 2 inches into the path to provide adequate clearance with the wall
            .addDisplacementMarker(2, () -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            // drive to the junction
            .splineToConstantHeading(new Vector2d(35.14, 44.05), Math.toRadians(-90.00))
            .splineToConstantHeading(new Vector2d(35.14, 30.00), Math.toRadians(-90.00))
            .splineTo(new Vector2d(33.00, 6.50), Math.toRadians(221.32))
            .completeTrajectory()
            // once the arm reaches the correct height, open the hand and then lower the arm to cone_high
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> Clock.sleep(100))
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH_LOWER))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(FourMotorArm::autoComplete)

            // SECOND CONE

            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_HIGH))
            .appendTrajectory()
            // drive to the cone stack
            .lineToSplineHeading(new Pose2d(38.92, 10.11, Math.toRadians(0.11)))
            .splineToConstantHeading(new Vector2d(59.00, 11.00), Math.toRadians(2.46))
            .completeTrajectory()
            // once the arm is at the right position, close in on the top cone and begin raising the arm. we also wait a fraction of a second so the arm has enough time to raise above the stack so when we drive backwards, the cone we've picked up doesnt knock over the whole stack
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoClose)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(500)
            .appendTrajectory()

            // SECOND JNCT

            // drive back to the junction
            .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
            .splineToConstantHeading(new Vector2d(30.58, 9.50), Math.toRadians(232.36))
            .completeTrajectory()
            // when the arm reaches the correct height, we open the hand again and then lower the arm back down to cone_high for another cycle
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> Clock.sleep(300))
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH_LOWER))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(FourMotorArm::autoLevelArm)
            .appendAction(FourMotorArm::autoComplete)

            // THIRD CONE

            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_MED))
            .appendTrajectory()
            // we drive to the cone stack
            .lineToSplineHeading(new Pose2d(37.68, 10.26, Math.toRadians(1.30)))
            .splineTo(new Vector2d(59.00, 11.13), Math.toRadians(0.00))
            .completeTrajectory()
            // same as last time: we wait for the arm to lower on the top of the stack, grab a cone, raise the arm, and then a fraction of a second later we begin driving back to the junction
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoClose)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(500)
            .appendTrajectory()

            // THIRD JNCT

            // drive back to the junction for the last time
            .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
            .splineToConstantHeading(new Vector2d(27.23, 9.70), Math.toRadians(228.78))
            .completeTrajectory()
            // once the arm is at the correct height, we open the hand and then lower the arm to the reset position; we're done cycling at this point and need to park
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(() -> Clock.sleep(150))
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH_LOWER))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(FourMotorArm::autoComplete)

            // PARKING --- SPOT 2 PART 1

            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET))
            .appendTrajectory()
            // we drive close to the second parking position
            .lineToSplineHeading(new Pose2d(34.28, 11.20, Math.toRadians(267.34)))
            .completeTrajectory()
            .complete();

        // if the signal was at position 2, we should just drive back a little bit
        // if the signal was at position 1, we drive to the left (driver pov) side and back
        // if the signal was at position 3, we drive to the right (driver pov) side and back a whole nother tile

        Auto one = new Auto(auto.end())
            .begin()
            .lineToConstantHeading(new Vector2d(57.77, 10.17))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        Auto two = new Auto(auto.end())
            .begin()
            .lineToConstantHeading(new Vector2d(34.28, 14.20))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        Auto three = new Auto(auto.end())
            .begin()
            .turn(Math.toRadians(90.00))
            .lineToConstantHeading(new Vector2d(8.00, 11.00))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        runner = new AutoRunner(auto, auto.getDrivetrain(), one, two, three);
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
