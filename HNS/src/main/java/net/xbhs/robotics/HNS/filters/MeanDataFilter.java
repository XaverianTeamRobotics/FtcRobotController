package net.xbhs.robotics.HNS.filters;

import net.xbhs.robotics.HNS.Localizer;
import net.xbhs.robotics.HNS.roles.TimestampedLocalizer;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;

public class MeanDataFilter extends NavigationFilter {
    long t_offset = 0;
    ArrayList<TimestampedLocalizer> history = new ArrayList<>();

    public MeanDataFilter(Localizer localizer) {
        super(localizer);
        t_offset = System.currentTimeMillis();
        history.add(new TimestampedLocalizer(localizer, System.currentTimeMillis() - t_offset));
    }

    @Override
    public Localizer correct(Localizer localizer, double dt) {
        return correct();
    }

    @Override
    public void update(Localizer localizer, double dt) {
        update(localizer);
    }

    public void update(Localizer localizer) {
        history.add(new TimestampedLocalizer(localizer, System.currentTimeMillis() - t_offset));
        this.currentLocalizer = localizer;
    }

    @Override
    public Localizer getEstimate() {
        return correct();
    }

    @Override
    public Localizer getEstimate(double dt) {
        return correct();
    }

    @Override
    public Localizer correct() {
        Localizer toRet = new Localizer();
        long t = System.currentTimeMillis() - t_offset;

        // Get the average acceleration from the last 10 points of data
        double[] ax = new double[10];
        double[] ay = new double[10];
        double[] aAzimuth = new double[10];
        for (int i = 0; i < 10; i++) {
            TimestampedLocalizer tl = history.get(history.size() - i - 1);
            ax[i] = tl.x;
            ay[i] = tl.y;
            aAzimuth[i] = tl.azimuth;
        }

        double avgAX = 0;
        double avgAY = 0;
        double avgAAzimuth = 0;
        for (int i = 0; i < 10; i++) {
            avgAX += ax[i];
            avgAY += ay[i];
            avgAAzimuth += aAzimuth[i];
        }
        avgAX /= 10;
        avgAY /= 10;
        avgAAzimuth /= 10;
        toRet.aX = avgAX;
        toRet.aY = avgAY;
        toRet.aAzimuth = avgAAzimuth;

        toRet.vX = avgAX * (t - history.get(history.size() - 1).getTime());
        toRet.vY = avgAY * (t - history.get(history.size() - 1).getTime());
        toRet.vAzimuth = avgAAzimuth * (t - history.get(history.size() - 1).getTime());

        toRet.x = avgAX * Math.pow(t - history.get(history.size() - 1).getTime(), 2) / 2;
        toRet.y = avgAY * Math.pow(t - history.get(history.size() - 1).getTime(), 2) / 2;
        toRet.azimuth = avgAAzimuth * Math.pow(t - history.get(history.size() - 1).getTime(), 2) / 2;

        return toRet;
    }

}