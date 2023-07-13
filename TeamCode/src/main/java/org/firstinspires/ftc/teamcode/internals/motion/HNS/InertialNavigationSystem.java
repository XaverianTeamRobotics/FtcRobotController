package org.firstinspires.ftc.teamcode.internals.motion.HNS;

import net.xbhs.robotics.HNS.*;
import net.xbhs.robotics.HNS.filters.PolynomailFilter;
import net.xbhs.robotics.HNS.filters.RobotKalmanFilter;
import net.xbhs.robotics.HNS.limitations.HNSRequiresZero;
import net.xbhs.robotics.HNS.roles.HNSRole_PrimaryNavigator;
import org.firstinspires.ftc.teamcode.internals.misc.Vector3;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.getImu;

@HNSCompatible
@HNSRequiresZero
@HNSRole_PrimaryNavigator
public class InertialNavigationSystem extends NavigationSystem {
    public PolynomailFilter filter = new PolynomailFilter(localizer);
    private ControlHubOrientation orientation;
    double dt;
    double previousAZ = 0;
    double previousVAZ = 0;

    public InertialNavigationSystem(ControlHubOrientation orientation, double dt) {
        super("INS");
        this.orientation = orientation;
        this.dt = dt;
    }

    public InertialNavigationSystem(ControlHubOrientation orientation) {
        this(orientation, 0.100);
    }

    public InertialNavigationSystem() {
        this(ControlHubOrientation.UPRIGHT);
    }

    @Override
    public void update() throws NavigationSystemException {
        // Get the acceleration data from the IMU
        Vector3 acceleration = getImu().getAcceleration();
        // Get the orientation data from the IMU
        Vector3 orientation = getImu().getOrientation();

        // Get aX and aY from the acceleration data, depending on the orientation of the control hub
        double aX = this.orientation == ControlHubOrientation.FLAT ? acceleration.component2() : acceleration.component1();
        double aY = this.orientation == ControlHubOrientation.FLAT ? acceleration.component1() : acceleration.component2();

        // Get the azimuth from the orientation data, depending on the orientation of the control hub
        double azimuth = this.orientation == ControlHubOrientation.FLAT ? orientation.component3() : orientation.component2();

        // Get the angular velocity of the azimuth by calculating using dt
        double azimuthVelocity = (azimuth - previousAZ) / dt;
        // Get the angular acceleration of the azimuth by calculating using dt
        double azimuthAcceleration = (azimuthVelocity - previousVAZ) / dt;

        // Use the azimuth to change aX and aY to be field-centric
        double aXField = aX * Math.cos(azimuth) - aY * Math.sin(azimuth);
        double aYField = aX * Math.sin(azimuth) + aY * Math.cos(azimuth);

        // Predict vX, vY, x, and y using dV = a * dt and dX = v*dt + 1/2 * a * dt^2
        double vX = localizer.vX + aXField * dt;
        double vY = localizer.vY + aYField * dt;
        double x = localizer.x + localizer.vX * dt + 0.5 * aXField * dt * dt;
        double y = localizer.y + localizer.vY * dt + 0.5 * aYField * dt * dt;

        // Update the localizer with the new values
        localizer.setPose(x, y, azimuth, vX, vY, 0);
        localizer.setAcceleration(aXField, aYField, 0);

        // Update the Kalman Filter
        filter.update(localizer, dt);

        // Correct the localizer with the new values
        localizer = filter.correct();
        localizer.azimuth = azimuth;
        localizer.vAzimuth = azimuthVelocity;
        localizer.aAzimuth = azimuthAcceleration;

        // Update the azimuth previous values
        previousAZ = localizer.azimuth;
        previousVAZ = localizer.vAzimuth;
    }

    @Override
    public void start() throws NavigationSystemException {
        // Verify that the IMU is present and initialized
        if (getImu() == null) throwException("IMU not initialized");
    }

    @Override
    public void correct(Localizer localizer) throws NavigationSystemException {
        this.localizer = localizer;
    }

    public enum ControlHubOrientation {
        FLAT,
        UPRIGHT,
    }
}
