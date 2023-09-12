package org.firstinspires.ftc.teamcode.internals.motion.pid.constrained;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.internals.image.PoleNavigator;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DashboardLogging;

/**
 * A profiled PID controller which attempts to center a specific pixel in an image.
 */
public class ImageFeedbackController {

    private final PoleNavigator localizer;
    private final ProfiledPIDController pidx, pidy;
    private final double pos_tx, pos_ty;

    /**
     * @param localizer The image-based localizer which returns the XY coordinates of a pixel such that the center of the image is the origin.
     * @param kP The proportional gain of the controller.
     * @param kI The integral gain of the controller.
     * @param kD The derivitave gain of the controller.
     * @param mV The maximum velocity of the controller.
     * @param mA The maximum acceleration of the controller.
     * @param sp The setpoint or goal.
     * @param cTol The tolerance of the setpoint.
     */
    public ImageFeedbackController(PoleNavigator localizer, double kP, double kI, double kD, double mV, double mA, double sp, double cTol, double eTol) {
        this(localizer, kP, kI, kD, mV, mA, sp, cTol, eTol, kP, kI, kD, mV, mA, sp, cTol, eTol);
    }

    public ImageFeedbackController(PoleNavigator localizer,
                                   double kPx, double kIx, double kDx, double mVx, double mAx, double spx, double cTolX, double eTolX,
                                   double kPy, double kIy, double kDy, double mVy, double mAy, double spy, double cTolY, double eTolY) {
        this.localizer = localizer;
        pos_tx = eTolX;
        pos_ty = eTolY;
        pidx = new ProfiledPIDController(kPx, kIx, kDx, new TrapezoidProfile.Constraints(mVx, mAx));
        pidx.setGoal(spx);
        pidx.setTolerance(cTolX);
        pidx.reset();
        pidy = new ProfiledPIDController(kPy, kIy, kDy, new TrapezoidProfile.Constraints(mVy, mAy));
        pidy.setGoal(spy);
        pidy.setTolerance(cTolY);
        pidy.reset();
        this.localizer.startStreaming();
    }

    /**
     * Calculates the output of the controller based on the output of the localizer.
     * @return An array of 2 elements containing the X and Y outputs (in that order).
     */
    public double[] calculate() {
        double x = localizer.getPoleDistanceX();
        double y = localizer.getPoleDistanceY();
        // this sometimes works, sometimes not :((
        if(x == Double.NaN || y == Double.NaN) {
            return new double[] { 0, 0 };
        }
        double width = localizer.getSize()[0];
        double height = localizer.getSize()[1];

        // normalize the coordinate to [-1, 1]
        double max, min;
        if(height < width) {
            min = 0 - width / 2.0;
            max = width - width / 2.0;
        }else{
            min = 0 - height / 2.0;
            max = height - height / 2.0;
        }
        x = -Range.scale(x, min, max, -100, 100);
        y = -Range.scale(y, min, max, -100, 100);

        double vx = pidx.calculate(x);
        double vy = pidy.calculate(y);
        vx = Range.clip(vx, -0.13, 0.13);
        vy = Range.clip(vy, -0.1, 0.1);
        if(pidx.atSetpoint()) {
            vx = 0;
        }
        if(pidy.atSetpoint()) {
            vy = 0;
        }

        DashboardLogging.log("x", x);
        DashboardLogging.log("y", y);
        DashboardLogging.log("width", width);
        DashboardLogging.log("height", height);
        DashboardLogging.log("vx", vx);
        DashboardLogging.log("vy", vy);
        DashboardLogging.update();

        return new double[] { vx, vy, pidx.atGoalWithSpecificTolerance(pos_tx, Double.POSITIVE_INFINITY) && pidy.atGoalWithSpecificTolerance(pos_ty, Double.POSITIVE_INFINITY) ? 1 : 0 };
    }

}

