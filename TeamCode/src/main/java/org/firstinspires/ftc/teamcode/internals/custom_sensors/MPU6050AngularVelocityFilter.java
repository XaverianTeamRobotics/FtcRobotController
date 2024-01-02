package org.firstinspires.ftc.teamcode.internals.custom_sensors;

import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class MPU6050AngularVelocityFilter {
	private KalmanFilter kalmanFilter;
	private double lastTime;

	public MPU6050AngularVelocityFilter() {
		this.kalmanFilter = initializeKalmanFilter();
		this.lastTime = System.currentTimeMillis();
	}

	private KalmanFilter initializeKalmanFilter() {
		// Define the initial state estimate (x0)
		double[][] initialStateEstimate2D = new double[][]{
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
		};

		// Define the initial error covariance estimate (P0)
		double[][] initialErrorCovariance = new double[][]{
			{1, 0, 0},
			{0, 1, 0},
			{0, 0, 1}
		};

		// Define the process noise (Q)
		double[][] processNoise = new double[][]{
			{0.1, 0, 0},
			{0, 0.1, 0},
			{0, 0, 0.1}
		};

		// Define the measurement noise (R)
		double[][] measurementNoise = new double[][]{
			{0.01, 0, 0},
			{0, 0.01, 0},
			{0, 0, 0.01}
		};

		// Define the measurement matrix (H)
		double[][] measurementMatrix = new double[][]{
			{1, 0, 0},
			{0, 1, 0},
			{0, 0, 1}
		};

		// Create the process and measurement models
		DefaultProcessModel processModel = new DefaultProcessModel(
		   processNoise,
		   initialStateEstimate2D,
		   initialErrorCovariance
		);
		DefaultMeasurementModel measurementModel = new DefaultMeasurementModel(
			new Array2DRowRealMatrix(measurementMatrix),
			new Array2DRowRealMatrix(measurementNoise)
		);

		// Create and return the Kalman filter
		return new KalmanFilter(processModel, measurementModel);
	}

	public double[] getOrientation(double[] angularVelocity) {
		double currentTime = System.currentTimeMillis();
		double dt = (currentTime - lastTime) / 1000.0; // Convert milliseconds to seconds
		lastTime = currentTime;

		RealVector u = new ArrayRealVector(new double[]{angularVelocity[0] * dt, angularVelocity[1] * dt, angularVelocity[2] * dt});

		kalmanFilter.predict(u);
		kalmanFilter.correct(u);

		return kalmanFilter.getStateEstimation();
	}
}