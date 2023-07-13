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

        return toRet;
    }

}