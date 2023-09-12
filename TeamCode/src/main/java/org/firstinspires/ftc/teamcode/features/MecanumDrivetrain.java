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
    private final boolean DRIVER_ASSIST;
    private AutonomousDrivetrain drivetrain;
    public static double xMult = 0.6, yMult = 0.6, rMult = 0.6;
    public static double xYMin = 7, xYMax = 1;
    public static double yYMin = 7, yYMax = 1;
    public static double pMin = FourMotorArm.ArmPosition.RESET.getHeight(), pMax = FourMotorArm.ArmPosition.JNCT_HIGH.getHeight();
    public static boolean simulated = false;
    private final SlewRateLimiter rlX = new SlewRateLimiter(1);
    private final SlewRateLimiter rlY = new SlewRateLimiter(1);
    private final RatelimitCalc rcX = new RatelimitCalc(xYMin, xYMax, pMin, pMax);
    private final RatelimitCalc rcY = new RatelimitCalc(yYMin, yYMax, pMin, pMax);

    public MecanumDrivetrain(boolean fieldCentric, boolean driverAssist) {
        FIELD_CENTRIC = fieldCentric;
        DRIVER_ASSIST = driverAssist;
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
        // dampen if assist is enabled
        if(DRIVER_ASSIST) {
            double[] v = dampen(x / 100, y / 100);
            x = Range.clip(v[0] * 100, -100, 100);
            y = Range.clip(v[1] * 100, -100, 100);
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

    private double @NotNull [] dampen(double x, double y) {
        int pos = Devices.encoder5.getPosition();
        return new double[] { limit(x, pos, rlX, rcX), limit(y, pos, rlY, rcY) };
    }

    private double limit(double vel, int pos, SlewRateLimiter limiter, RatelimitCalc calculator) {
        return limit(vel, pos, limiter, calculator, null);
    }

    /**
     * Allows debug logging to both DS and Dashboard via Logging.log($logger, $rate) where $logger is the logger argument (the caption of the data) and $rate is the rate double calculated in the method.
     */
    private double limit(double vel, int pos, SlewRateLimiter limiter, RatelimitCalc calculator, String logger) {
        double rate = calculator.calculate(pos);
        if(logger != null) {
            DashboardLogging.log(logger, rate);
        }
        limiter.setRateLimit(rate);
        return limiter.calculate(vel);
    }

}
