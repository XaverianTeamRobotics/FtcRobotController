package net.xbhs.robotics.HNS.filters;

import net.xbhs.robotics.HNS.Localizer;

public abstract class NavigationFilter {
    // Estimated error gain. 0 = any jerk is 0% error, 1 = the jerk is equal to our % confidence
    protected double errorGain = 0.25;
    public Localizer currentLocalizer;
    protected double dt;

    public NavigationFilter(Localizer localizer) {
        this.currentLocalizer = localizer;
        this.dt = 0;
    }

    public void update(Localizer localizer, double dt) {
        this.currentLocalizer = localizer;
        this.dt = dt;
    }

    /**
     * Use the basic kinematic equations to estimate the position of the robot
     *
     * @return The estimated position of the robot
     */
    public Localizer getEstimate() {
        Localizer estimate = new Localizer();
        estimate.setPose(
                currentLocalizer.x + currentLocalizer.vX * dt + currentLocalizer.aX * dt * dt / 2,
                currentLocalizer.y + currentLocalizer.vY * dt + currentLocalizer.aY * dt * dt / 2,
                currentLocalizer.azimuth + currentLocalizer.vAzimuth * dt + currentLocalizer.aAzimuth * dt * dt / 2,
                currentLocalizer.vX + currentLocalizer.aX * dt,
                currentLocalizer.vY + currentLocalizer.aY * dt,
                currentLocalizer.vAzimuth + currentLocalizer.aAzimuth * dt);
        return estimate;
    }

    /**
     * Use the basic kinematic equations to estimate the position of the robot
     *
     * @param dt The time since the last update
     * @return The estimated position of the robot
     */
    public Localizer getEstimate(double dt) {
        double oldDT = this.dt;
        this.dt = dt;
        Localizer estimate = getEstimate();
        this.dt = oldDT;
        return estimate;
    }

    /**
     * Compute the jerk in acceleration and use it to adjust the estimated % confidence in our predictions
     *
     * @return The estimated confidence of the algorithm. 0 = no error in sensor data, 1 = sensor cannot be trusted. [x, y, azimuth]
     */
    protected double[] getEstimatedConfidence(Localizer localizer, double dt) {
        double jerkX = (localizer.aX - this.currentLocalizer.aX) / dt;
        double jerkY = (localizer.aY - this.currentLocalizer.aY) / dt;
        double jerkAzimuth = (localizer.aAzimuth - this.currentLocalizer.aAzimuth) / dt;
        double[] error = {0, 0, 0};
        error[0] = Math.min(Math.abs(jerkX * errorGain), 1);
        error[1] = Math.min(Math.abs(jerkY * errorGain), 1);
        error[2] = Math.min(Math.abs(jerkAzimuth * errorGain), 1);
        return error;
    }

    public abstract Localizer correct(Localizer localizer, double dt);

    public Localizer correct() {
        currentLocalizer = correct(currentLocalizer, dt);
        return currentLocalizer;
    }
}
