package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.JCam;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.MultipleCameraManager;
import org.firstinspires.ftc.teamcode.internals.image.PoleLocalizer;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;

import java.util.Objects;
import java.util.UUID;

import static org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain.SLOW_ACCEL_CONSTRAINT;
import static org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain.SLOW_VEL_CONSTRAINT;

public class AutoRight extends OperationMode implements AutonomousOperation {

    Timer time;
    int spot = 0;
    SleeveDetector sleeve;
    boolean findingSleeve = true;
    AutoRunner runner;
    AutonomousDrivetrain drivetrain = null;

    @Override
    public void construct() {
        MultipleCameraManager.reset();
        time = Clock.make(UUID.randomUUID().toString());
        Hand hand = new Hand(true);
        registerFeature(hand);
        FourMotorArm arm = new FourMotorArm();
        registerFeature(arm);
        sleeve = new SleeveDetector(0);
        registerFeature(sleeve);
        registerFeature(new JCam());
        Pose2d start = new Pose2d(35.84, 61.50, Math.toRadians(-90.00));
        PoleLocalizer poleLocalizer = new PoleLocalizer(1);
        poleLocalizer.startStreaming();
        Auto auto = new Auto(start)

            // FIRST CONE
            // its preloaded, we do nothing :D

            .begin()

            // FIRST JNCT

            // when we're 2 inches into the path, raise the arm. we do ths 2 inches into the path to provide adequate clearance with the wall
            .addDisplacementMarker(4, () -> {
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH);
                JCam.toggle();
            })
            // drive to the junction
            .splineToConstantHeading(new Vector2d(36.54, 44.05), Math.toRadians(-90.00))
            .splineToConstantHeading(new Vector2d(36.54, 30.00), Math.toRadians(-90.00))
            .splineTo(new Vector2d(37.01, 10.00), Math.toRadians(318.68))
            .completeTrajectory()
            // center ourselves on the pole
            .appendWait(FourMotorArm::autoComplete)
            .appendWait(JCam::complete)
            .appendWait(1000)
            .appendAction(() -> this.driveToPole(poleLocalizer))
            .appendAction(JCam::toggle)
            // open the hand and then lower the arm to cone_high
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
            .lineToSplineHeading(new Pose2d(32.76, 11.11, Math.toRadians(179.89)))
            .splineToConstantHeading(new Vector2d(12.68, 12.00), Math.toRadians(0))
            .completeTrajectory()
            // once the arm is at the right position, close in on the top cone and begin raising the arm. we also wait a fraction of a second so the arm has enough time to raise above the stack so when we drive backwards, the cone we've picked up doesnt knock over the whole stack
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoClose)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(500)
            .appendAction(JCam::toggle)
            .appendTrajectory()

            // SECOND JNCT

            // drive back to the junction
            .lineToSplineHeading(new Pose2d(34.30, 13.95, Math.toRadians(307.64)))
            .splineToConstantHeading(new Vector2d(37.10, 14.00), Math.toRadians(318.68))
            .completeTrajectory()
            // center ourselves on the pole
            .appendWait(FourMotorArm::autoComplete)
            .appendWait(JCam::complete)
            .appendWait(3000)
            .appendAction(() -> this.driveToPole(poleLocalizer))
            .appendAction(JCam::toggle)
            // when the arm reaches the correct height, we open the hand again and then lower the arm back down to cone_high for another cycle
            .appendAction(() -> Clock.sleep(300))
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH_LOWER))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(FourMotorArm::autoLevelArm)
            .appendAction(FourMotorArm::autoComplete)

            // PARKING --- SPOT 2 PART 1

            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET))
            .appendTrajectory()
            // we drive close to the second parking position
            .lineToSplineHeading(new Pose2d(37.40, 11.20, Math.toRadians(272.66)))
            .completeTrajectory()
            .complete();

        // if the signal was at position 2, we should just drive back a little bit
        // if the signal was at position 1, we drive to the left (driver pov) side and back
        // if the signal was at position 3, we drive to the right (driver pov) side and back a whole nother tile

        Auto one = new Auto(auto.end())
            .begin()
            .lineToConstantHeading(new Vector2d(13.91, 12.17))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        Auto two = new Auto(auto.end())
            .begin()
            .lineToConstantHeading(new Vector2d(37.40, 14.20))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        Auto three = new Auto(auto.end())
            .begin()
            .turn(Math.toRadians(90.00))
            .lineToConstantHeading(new Vector2d(56.68, 12.00))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        drivetrain = auto.getDrivetrain();
        runner = new AutoRunner(auto, drivetrain, one, two, three);
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

    private double[] generateVectorToPole(PoleLocalizer poleLocalizer, AutonomousDrivetrain drivetrain) {
        double[] d = null;
        while(d == null) {
            d = poleLocalizer.getData();
            Clock.sleep(10);
        }
        vec = new double[] { -d[1], -d[0] };
        return vec;
    }

    private double[] vec = null;

    public void driveToPole(PoleLocalizer poleLocalizer) {
        poleLocalizer.invalidate();
        TrajectorySequence t = drivetrain.trajectorySequenceBuilder(drivetrain.getPoseEstimate())
            .forward(generateVectorToPole(poleLocalizer, drivetrain)[0], SLOW_VEL_CONSTRAINT, SLOW_ACCEL_CONSTRAINT)
            .strafeLeft(vec[1], SLOW_VEL_CONSTRAINT, SLOW_ACCEL_CONSTRAINT)
            .forward(6, SLOW_VEL_CONSTRAINT, SLOW_ACCEL_CONSTRAINT)
            .completeTrajectory();
        drivetrain.followTrajectorySequenceAsync(t);
        while(drivetrain.isBusy() && Objects.requireNonNull(HardwareGetter.getOpMode()).opModeIsActive()) {
            drivetrain.update();
        }
        vec = null;
//        drivetrain.setPoseEstimate(t.end());
    }

}
