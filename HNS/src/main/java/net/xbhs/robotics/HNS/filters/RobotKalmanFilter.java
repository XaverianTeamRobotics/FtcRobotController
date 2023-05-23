package net.xbhs.robotics.HNS.filters;

import net.xbhs.robotics.HNS.Localizer;
import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;

/**
 * Create a Kalman Filter that can be used with NavigationSystem
 * Takes in a Localizer as its main parameter
 * Accounts for the following variables from the Localizer:
 * x, y, azimuth, vX, vY, vAzimuth, aX, aY, aAzimuth
 * The class will compute all 3 positions and velocities at the same time
 * <p>
 * <em>Inspiration</em> from <a href="https://commons.apache.org/proper/commons-math/userguide/filter.html">Apache Commons Math</a>
 * @author Michael L
 */
public class RobotKalmanFilter extends NavigationFilter {
    // Measurement noise
    // TODO: Calibrate
    public static double measurementNoise = 0.000001;

    // Acceleration noise
    // TODO: Calibrate
    public static double accelNoise = 5000;

    // Time between iterations (seconds)
    public double dt = 0.100;

    // Maximum acceleration (meters per second squared)
    public double maxAccel = 0.762;

    // A = [ 1 dt ]
    //     [ 0  1 ]
    RealMatrix A = new Array2DRowRealMatrix(new double[][] { { 1, dt }, { 0, 1 } });
    // B = [ dt^2/2 ]
    //     [ dt     ]
    RealMatrix B = new Array2DRowRealMatrix(new double[][] { { Math.pow(dt, 2d) / 2d }, { dt } });
    // H = [ 1 0 ]
    RealMatrix H = new Array2DRowRealMatrix(new double[][] { { 1d, 0d } });
    // x = [ 0 0 ]
    RealVector initialPosition = new ArrayRealVector(new double[] { 0, 0 });

    RealMatrix tmp = new Array2DRowRealMatrix(new double[][] {
            { Math.pow(dt, 4d) / 4d, Math.pow(dt, 3d) / 2d },
            { Math.pow(dt, 3d) / 2d, Math.pow(dt, 2d) } });
    // Q = [ dt^4/4 dt^3/2 ]
    //     [ dt^3/2 dt^2   ]
    RealMatrix Q = tmp.scalarMultiply(Math.pow(accelNoise, 2));
    // P0 = [ 1 1 ]
    //      [ 1 1 ]
    RealMatrix P0 = new Array2DRowRealMatrix(new double[][] { { 1, 1 }, { 1, 1 } });
    // R = [ measurementNoise^2 ]
    RealMatrix R = new Array2DRowRealMatrix(new double[] { Math.pow(measurementNoise, 2) });

    ProcessModel pm = new DefaultProcessModel(A, B, Q, initialPosition, P0);
    MeasurementModel mm = new DefaultMeasurementModel(H, R);
    KalmanFilter filterX = new KalmanFilter(pm, mm);
    KalmanFilter filterY = new KalmanFilter(pm, mm);
    KalmanFilter filterAZ = new KalmanFilter(pm, mm);

    public double xOffset = 0;
    public double yOffset = 0;
    public double azimuthOffset = 0;

    public double vxOffset = 0;
    public double vyOffset = 0;
    public double vazimuthOffset = 0;


    public RobotKalmanFilter(Localizer localizer) {
        super(localizer);
    }

    // A list of the accelerations (and their dt) over the past 50 cycles
    // Used to compute the average acceleration over the past 50 cycles
    // This is used to compute the control input
    ArrayList<double[]> accelHistory = new ArrayList<>();

    // Overwrite the update method to compute accelHistory
    @Override
    public void update(Localizer localizer, double dt) {
        super.update(localizer, dt);
        accelHistory.add(new double[] { localizer.aX, localizer.aY, localizer.aAzimuth, dt });
        if (accelHistory.size() > 10) {
            accelHistory.remove(0);
        }
        this.dt = dt;
    }

    /**
     * Correct the localizer given and return the corrected localizer
     * Computes a waited average based on the estimated confidence
     * @param sensorLocalizer The localizer to correct
     * @return The corrected localizer
     */
    @Override
    public Localizer correct(Localizer sensorLocalizer, double dt) {
        if (currentLocalizer.vX == 0 && currentLocalizer.vY == 0 && currentLocalizer.vAzimuth == 0 && currentLocalizer.aX == 0 && currentLocalizer.aY == 0 && currentLocalizer.aAzimuth == 0) {
            // If we have no velocity or accel, we need to recorrect the KalmanFilters

            xOffset = currentLocalizer.x;
            yOffset = currentLocalizer.y;
            azimuthOffset = currentLocalizer.azimuth;
            vxOffset = currentLocalizer.vX;
            vyOffset = currentLocalizer.vY;
            vazimuthOffset = currentLocalizer.vAzimuth;

            // System.out.println("[KALMAN] DEBUG - Recorrecting Kalman Filters");
            filterX = new KalmanFilter(pm, mm);
            filterY = new KalmanFilter(pm, mm);
            filterAZ = new KalmanFilter(pm, mm);

            return currentLocalizer;
        }
        // System.out.println("[KALMAN] DEBUG - dt: " + dt);

        // Compute the average acceleration over all the history we have
        double avgAccelX = 0;
        double avgAccelY = 0;
        double avgAccelAZ = 0;
        double totalDt = 0;

        for (double[] accel : accelHistory) {
            avgAccelX += accel[0] * accel[3];
            avgAccelY += accel[1] * accel[3];
            avgAccelAZ += accel[2] * accel[3];
            totalDt += accel[3];
        }

        avgAccelX /= totalDt;
        avgAccelY /= totalDt;
        avgAccelAZ /= totalDt;

        // control input: our current acceleration
        RealVector uX = new ArrayRealVector(new double[] { avgAccelX });
        RealVector uY = new ArrayRealVector(new double[] { avgAccelY });
        RealVector uAzimuth = new ArrayRealVector(new double[] { avgAccelAZ });

        // If dt is not 1, we need to change the control input
        if (dt != 1) {
            uX = uX.mapMultiply(dt);
            uY = uY.mapMultiply(dt);
            uAzimuth = uAzimuth.mapMultiply(dt);
        }

        // Predict
        filterX.predict(uX);
        filterY.predict(uY);
        filterAZ.predict(uAzimuth);

        // Correct
        filterX.correct(new ArrayRealVector(new double[] { sensorLocalizer.x }));
        filterY.correct(new ArrayRealVector(new double[] { sensorLocalizer.y }));
        filterAZ.correct(new ArrayRealVector(new double[] { sensorLocalizer.azimuth }));

        Localizer correctedLocalizer = new Localizer();
        correctedLocalizer.x = filterX.getStateEstimation()[0] + xOffset;
        correctedLocalizer.y = filterY.getStateEstimation()[0] + yOffset;
        correctedLocalizer.azimuth = filterAZ.getStateEstimation()[0] + azimuthOffset;

        correctedLocalizer.vX = filterX.getStateEstimation()[1] + vxOffset;
        correctedLocalizer.vY = filterY.getStateEstimation()[1] + vyOffset;
        correctedLocalizer.vAzimuth = filterAZ.getStateEstimation()[1] + vazimuthOffset;

        correctedLocalizer.aX = sensorLocalizer.aX;
        correctedLocalizer.aY = sensorLocalizer.aY;
        correctedLocalizer.aAzimuth = sensorLocalizer.aAzimuth;

        return correctedLocalizer;
    }
}
