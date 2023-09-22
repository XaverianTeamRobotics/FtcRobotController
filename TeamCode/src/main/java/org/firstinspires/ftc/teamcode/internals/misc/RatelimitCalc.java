package org.firstinspires.ftc.teamcode.internals.misc;

import com.qualcomm.robotcore.util.Range;

/**
 * Calculates a limit of a <a href="https://en.wikipedia.org/wiki/Slew_rate">slew rate</a> such that <kbd>limit = ln(weight)</kbd> where the logarithm is within a certain area. Check this <a href="https://www.desmos.com/calculator/rziziaen6n">visualization</a> for more details.
 */
public class RatelimitCalc {

    private final double x1 = 1, y1, x2 = 101, y2;
    private final double min, max;

    public RatelimitCalc(double y1, double y2, double minWeight, double maxWeight) {
        this.y1 = y1;
        this.y2 = y2;
        min = minWeight;
        max = maxWeight;
    }

    public double calculate(int position) {
        double pos = normalizePosition(position);
        return internalCalc(pos);
    }

    private double normalizePosition(int p) {
        p = Range.clip(p, (int) min, (int) max);
        return Range.scale(p, min, max, 1, 101);
    }

    private double internalCalc(double p) {
        return f(p);
    }

    private double a() {
        //       numerator of m
        // a = ------------------
        //      ln of denom of m
        return (y1 - y2) / Math.log(x1 / x2);
    }

    private double b() {
        //      y2 * ln of x1 - y1 * ln of x2
        // b = -------------------------------
        //                 y1 - y2
        return Math.exp((y2 * Math.log(x1) - y1 * Math.log(x2)) / (y1 - y2));
    }

    private double f(double x) {
        //
        // f = a * ln of (b * x) as long as its on [x1, x2], otherwise its y1 or y2
        //
        if(x < x1) {
            return y1;
        }else if(x <= x2) {
            return a() * Math.log(b() * x);
        }else{
            return y2;
        }
    }

}
