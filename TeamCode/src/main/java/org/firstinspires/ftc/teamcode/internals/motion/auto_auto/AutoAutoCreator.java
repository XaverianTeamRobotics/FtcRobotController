package org.firstinspires.ftc.teamcode.internals.motion.auto_auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.ArmClaw;
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.VisionPipeline;
import org.firstinspires.ftc.teamcode.internals.image.centerstage.SpikeMarkDetectionPipeline;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;
import org.firstinspires.ftc.teamcode.opmodes.LasagnaBot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.toRadians;

public class AutoAutoCreator extends OperationMode implements AutonomousOperation {

    private AutoAutoCreatorConfig config;
    Timer time;
    AutoRunner runner;

    private final Vector2d backdrop = new Vector2d(48.00, 36.00);
    private final Vector2d spikeMark = new Vector2d(12.00, 36.00);
    private final Vector2d spikeMark2 = new Vector2d(-36.00, 36.00);
    private final Vector2d leftPark = new Vector2d(60.00, 60.00);
    private final Vector2d rightPark = new Vector2d(60.00, 12.00);
    private final Vector2d middlePark = new Vector2d(48.00, 36.00);

    private final Vector2d redBackdrop = new Vector2d(backdrop.getX(), -backdrop.getY());
    private final Vector2d redSpikeMark = new Vector2d(spikeMark2.getX(), -spikeMark2.getY());
    private final Vector2d redSpikeMark2 = new Vector2d(spikeMark.getX(), -spikeMark.getY());
    private final Vector2d redLeftPark = new Vector2d(rightPark.getX(), -rightPark.getY());
    private final Vector2d redRightPark = new Vector2d(leftPark.getX(), -leftPark.getY());
    private final Vector2d redMiddlePark = new Vector2d(middlePark.getX(), -middlePark.getY());
    private ArmClaw armClaw;
    private VisionProcessingFeature visionProcessor;
	private int spot = 0;

    private final ArrayList<Vector2d> pois = new ArrayList<>();

    @Override
    public Class<? extends OperationMode> getNext() {
        return LasagnaBot.class;
    }

    @Override
    public void construct() {
        armClaw = new ArmClaw();
        visionProcessor = new VisionProcessingFeature(new SpikeMarkDetectionPipeline());

        time = Clock.make(UUID.randomUUID().toString());
        config = new AutoAutoCreatorConfig();
        config.askQuestions();
        if (!config.isValid()) throw new RuntimeException("Invalid auto auto config");
        AutoNoNavigationZones.addCenterstageDefaults();

        visionProcessor.setTeamColor(config.getTeamColor() == 0 ? VisionPipeline.TeamColor.BLUE : VisionPipeline.TeamColor.RED);
        registerFeature(armClaw);
        registerFeature(visionProcessor);

        // Change the values based on the team color
        Vector2d backdrop = config.getTeamColor() == 0 ? this.backdrop : redBackdrop;
        Vector2d leftPark = config.getTeamColor() == 0 ? this.leftPark : redLeftPark;
        Vector2d rightPark = config.getTeamColor() == 0 ? this.rightPark : redRightPark;
        Vector2d middlePark = config.getTeamColor() == 0 ? this.middlePark : redMiddlePark;

        if (config.getPlaceBackdrop()) pois.add(backdrop);

        if (config.getParkPlace() == 0) pois.add(leftPark);
        else if (config.getParkPlace() == 2) pois.add(rightPark);
        else if (config.getParkPlace() == 1) pois.add(middlePark);

        double y = (config.getTeamColor() == 0 ? 1 : -1) * AutoAutoPathSegment.START_L_Y;
        double rot = config.getTeamColor() == 0 ? toRadians(-90.00) : toRadians(90.00);
        boolean xStartingPos = config.getStartingPosition() == 0;
        if (config.getTeamColor() == 1) xStartingPos = !xStartingPos;
        double x = xStartingPos ? AutoAutoPathSegment.START_L_X : -36;
        Pose2d start = new Pose2d(x, y, rot);

        telemetry.setAutoClear(false);

		Auto auto = new Auto(start);
        AutonomousDrivetrain drivetrain = auto.getDrivetrain();

        TrajectorySequenceBuilder builder = auto.begin();

        Logging.log("Calculating path...");
        Logging.update();
        long startT = System.currentTimeMillis();
        BestPathFinder.populate();
        Vector2d last = start.vec();

        if (config.getPlaceSpikeMark()) {
            builder = builder.forward(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK);
            builder = builder.completeTrajectory().appendTrajectory();
            // DO SPIKE MARK LOGIC!!!!
            // WE MUST RETURN TO THE ORIGINAL POSITION IN OUR ORIGINAL ROTATION!!!!!!!
            builder = builder.back(AutoAutoPathSegment.DISTANCE_TO_SPIKE_MARK);
            builder = builder.completeTrajectory().appendTrajectory();
        }

        boolean needToScore = config.getPlaceBackdrop();
        for (Vector2d poi : pois) {
            ArrayList<AutoAutoPathSegment> path = BestPathFinder.getFastestPathToPoint(last, poi, 0);
            for (AutoAutoPathSegment segment : path) {
                Logging.log("Adding Path Segment " + segment.getClass().getSimpleName());
                Logging.update();
                try {
                    builder = segment.addPathSegment(builder);
                    last = segment.getEndPosition();
                } catch (Exception ignored) {
                    emergencyStop("Failed to add " + segment.getClass().getSimpleName());
                }

                if ((segment.getEndPosition().getY() == 36.00 || segment.getEndPosition().getY() == -36.00)
                        && segment.getEndPosition().getX() == 48.00
                        && needToScore) {
                    Runnable action = () -> {
                        Pose2d p = drivetrain.getPoseEstimate();
                        TrajectorySequenceBuilder b = drivetrain.trajectorySequenceBuilder(p);
                        armClaw.autoRaiseArm(ArmClaw.KeyPositions.FOUR);
                        armClaw.autoRotateClaw1(3); // TODO: Replace value with actual value
                        armClaw.autoRotateClaw2(25); // TODO: Replace value with actual value

                        // Wait for the arm to finish moving
                        waitUntil(() -> armClaw.isArmLiftingInProgress());

                        double dist = armClaw.getArmDistanceSensor() / 2.54;
                        b.forward(dist);

                        if (spot == 1) b.strafeLeft(5); // TODO: Replace value with actual value
                        if (spot == 3) b.strafeRight(5); // TODO: Replace value with actual value

                        if (config.getBackdropPixelPosition() == 1) armClaw.openLeftGrabber();
                        else armClaw.openRightGrabber();

                        b.back(dist);

                        drivetrain.followTrajectorySequenceAsync(b.completeTrajectory());
                        while(drivetrain.isBusy() && Objects.requireNonNull(HardwareGetter.getOpMode()).opModeIsActive()) {
                            drivetrain.update();
                        }
                    };
                    builder.completeTrajectory().appendAction(action).appendTrajectory();
                    needToScore = false;
                }
            }
        }
        Logging.log("Calculated path in " + (System.currentTimeMillis() - startT) + "ms");
        Logging.update();

        auto = builder.completeTrajectory().complete();

        telemetry.setAutoClear(true);

        runner = new AutoRunner(auto, drivetrain, null, null, null);
    }

    @Override
    public void run() {
        if (spot == 0) runner.run();
        else spot = visionProcessor.getSpot();
    }
}
