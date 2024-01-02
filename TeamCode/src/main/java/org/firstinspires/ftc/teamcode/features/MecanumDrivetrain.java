package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.RatelimitCalc;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.SlewRateLimiter;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DashboardLogging;
import org.jetbrains.annotations.NotNull;

/**
 * A mecanum drivetrain. This relies on odometry. Use {@link NativeMecanumDrivetrain} if you don't have odometry.
 */
@Config
public class MecanumDrivetrain extends Feature implements Buildable {

    private final boolean FIELD_CENTRIC;
    private AutonomousDrivetrain drivetrain;
    public static double xMult = 0.6, yMult = 0.6, rMult = 0.6;
    public static double xYMin = 7, xYMax = 1;
    public static double yYMin = 7, yYMax = 1;
    public static boolean simulated = false;

    public MecanumDrivetrain(boolean fieldCentric) {
        FIELD_CENTRIC = fieldCentric;
    }

    @Override
    public void build() {
        drivetrain = new AutonomousDrivetrain();
        drivetrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.setPoseEstimate(PoseBucket.getPose());
    }

    @Override
    public void loop() {
        // Read current pose
        Pose2d poseEstimate = drivetrain.getPoseEstimate();
        // Get gamepad inputs
        double x = Devices.controller1.getLeftStickX() * xMult;
        double y = Devices.controller1.getLeftStickY() * yMult;
        double r = Devices.controller1.getRightStickX() * rMult;
        DashboardLogging.logData("iX", x);
        DashboardLogging.logData("iY", y);
        DashboardLogging.logData("iR", r);
        boolean reset = Devices.controller1.getTouchpad();
        if(reset) {
            drivetrain.setPoseEstimate(new Pose2d(0, 0, 0));
        }
        if(!simulated) {
            // Create a vector from the gamepad x/y inputs
            // Then, rotate that vector by the inverse of that heading if we're using field centric--otherwise we'll just assume the heading is 0
            Vector2d input;
            if(FIELD_CENTRIC) {
                input = new Vector2d(
                    -Compressor.compress(y),
                    -Compressor.compress(x)
                ).rotated(-poseEstimate.getHeading());
            }else{
                input = new Vector2d(
                    -Compressor.compress(y),
                    -Compressor.compress(x)
                );
            }
            // Pass in the rotated input + right stick value for rotation
            // Rotation is not part of the rotated input thus must be passed in separately
            drivetrain.setWeightedDrivePower(
                new Pose2d(
                    input.getX(),
                    input.getY(),
                    -Compressor.compress(r)
                )
            );
        }
        // Update everything. Odometry. Etc.
        DashboardLogging.logData("oX", x);
        DashboardLogging.logData("oY", y);
        DashboardLogging.logData("oR", r);
        DashboardLogging.update();
        drivetrain.update();
    }
}
