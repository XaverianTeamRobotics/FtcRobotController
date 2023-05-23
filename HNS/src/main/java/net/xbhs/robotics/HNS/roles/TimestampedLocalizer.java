package net.xbhs.robotics.HNS.roles;

import net.xbhs.robotics.HNS.Localizer;

public class TimestampedLocalizer extends Localizer {
    long time;

    public double getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TimestampedLocalizer(long time) {
        this.time = time;
    }

    public TimestampedLocalizer(double x, double y, double azimuth, long time) {
        super(x, y, azimuth);
        this.time = time;
    }

    public TimestampedLocalizer(double x, double y, double azimuth, double vX, double vY, double vAzimuth, long time) {
        super(x, y, azimuth, vX, vY, vAzimuth);
        this.time = time;
    }

    public TimestampedLocalizer(double x, double y, double azimuth, double vX, double vY, double vAzimuth, double aX, double aY, double aAzimuth, long time) {
        super(x, y, azimuth, vX, vY, vAzimuth, aX, aY, aAzimuth);
        this.time = time;
    }

    public TimestampedLocalizer(Localizer localizer, long time) {
        this.x = localizer.x;
        this.y = localizer.y;
        this.azimuth = localizer.azimuth;
        this.vX = localizer.vX;
        this.vY = localizer.vY;
        this.vAzimuth = localizer.vAzimuth;
        this.aX = localizer.aX;
        this.aY = localizer.aY;
        this.aAzimuth = localizer.aAzimuth;
        this.time = time;
    }
}
