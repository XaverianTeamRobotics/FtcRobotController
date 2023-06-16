package net.xbhs.robotics.HNS.filters;

import net.xbhs.robotics.HNS.Localizer;
import net.xbhs.robotics.HNS.roles.TimestampedLocalizer;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;

public class PolynomailFilter extends NavigationFilter {
    long t_offset = 0;
    ArrayList<TimestampedLocalizer> history = new ArrayList<>();

    PolynomialFunction xPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction yPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction azimuthPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});

    PolynomialFunction vXPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction vYPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction vAzimuthPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});

    PolynomialFunction aXPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction aYPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});
    PolynomialFunction aAzimuthPoly = new PolynomialFunction(new double[]{0, 0, 0, 0, 0, 0});

    public PolynomailFilter(Localizer localizer) {
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
        // Calculate the expected acceleration using the polynomial
        double aX_expected = aXPoly.value(t);
        double aY_expected = aYPoly.value(t);
        double aAzimuth_expected = aAzimuthPoly.value(t);

        // Get the average acceleration between the current localizer and the expected acceleration
        toRet.aX = (aX_expected + currentLocalizer.aX) / 2;
        toRet.aY = (aY_expected + currentLocalizer.aY) / 2;
        toRet.aAzimuth = (aAzimuth_expected + currentLocalizer.aAzimuth) / 2;

        // Calculate the expected velocity using the polynomial
        double vX_expected = vXPoly.value(t);
        double vY_expected = vYPoly.value(t);
        double vAzimuth_expected = vAzimuthPoly.value(t);

        // Calculate the velocity estimate for 0.25 seconds ago and add that to the acceleration estimate * 0.25
        toRet.vX = vX_expected + toRet.aX * 0.25;
        toRet.vY = vY_expected + toRet.aY * 0.25;
        toRet.vAzimuth = vAzimuth_expected + toRet.aAzimuth * 0.25;

        // Average the expected v and the calculated v
        toRet.vX = (toRet.vX + vX_expected) / 2;
        toRet.vY = (toRet.vY + vY_expected) / 2;
        toRet.vAzimuth = (toRet.vAzimuth + vAzimuth_expected) / 2;

        // Calculate the expected velocity using the polynomial
        double X_expected = xPoly.value(t);
        double Y_expected = yPoly.value(t);
        double Azimuth_expected = azimuthPoly.value(t);

        // Calculate the velocity estimate for 0.25 seconds ago and add that to the acceleration estimate * 0.25
        toRet.x = X_expected + toRet.vX * 0.25;
        toRet.y = Y_expected + toRet.vY * 0.25;
        toRet.azimuth = Azimuth_expected + toRet.vAzimuth * 0.25;

        // Average the expected v and the calculated v
        toRet.x = (toRet.x + vX_expected) / 2;
        toRet.y = (toRet.y + vY_expected) / 2;
        toRet.azimuth = (toRet.azimuth + Azimuth_expected) / 2;

        // Update the history
        update(toRet);

        return toRet;
    }

    private void fitCurves() {
        // Create a table with the times and position, acceleration, and velocity
        ArrayList<WeightedObservedPoint> x = new ArrayList<>();
        ArrayList<WeightedObservedPoint> y = new ArrayList<>();
        ArrayList<WeightedObservedPoint> az = new ArrayList<>();
        ArrayList<WeightedObservedPoint> vX = new ArrayList<>();
        ArrayList<WeightedObservedPoint> vY = new ArrayList<>();
        ArrayList<WeightedObservedPoint> vAZ = new ArrayList<>();
        ArrayList<WeightedObservedPoint> aX = new ArrayList<>();
        ArrayList<WeightedObservedPoint> aY = new ArrayList<>();
        ArrayList<WeightedObservedPoint> aAZ = new ArrayList<>();
        for (TimestampedLocalizer i :
                history) {
            x.add(new WeightedObservedPoint(1, i.getTime(), i.x));
            y.add(new WeightedObservedPoint(1, i.getTime(), i.y));
            az.add(new WeightedObservedPoint(1, i.getTime(), i.azimuth));

            vX.add(new WeightedObservedPoint(1, i.getTime(), i.vX));
            vY.add(new WeightedObservedPoint(1, i.getTime(), i.vY));
            vAZ.add(new WeightedObservedPoint(1, i.getTime(), i.vAzimuth));

            aX.add(new WeightedObservedPoint(1, i.getTime(), i.aX));
            aY.add(new WeightedObservedPoint(1, i.getTime(), i.aY));
            aAZ.add(new WeightedObservedPoint(1, i.getTime(), i.aAzimuth));
        }

        // Predict the next 10 points assuming a constant acceleration and add them to the list
        // Assume the time between points will be 50 ms, so predict half a second in the future
        for (int i=0; i<=500; i+=50) {
            x.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.x + (currentLocalizer.vX * i) + Math.pow((currentLocalizer.aX * i), 2)));
            y.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.y + (currentLocalizer.vY * i) + Math.pow((currentLocalizer.aY * i), 2)));
            az.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.azimuth + (currentLocalizer.vAzimuth * i) + Math.pow((currentLocalizer.aAzimuth * i), 2)));

            vX.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.vX + (currentLocalizer.aX * i)));
            vY.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.vY + (currentLocalizer.aY * i)));
            vAZ.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.vAzimuth + (currentLocalizer.aAzimuth * i)));

            aX.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.aX));
            aY.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.aY));
            aAZ.add(new WeightedObservedPoint(1, System.currentTimeMillis() + i, currentLocalizer.aAzimuth));
        }

        // Fit the curves
        xPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(x));
        yPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(y));
        azimuthPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(az));

        vXPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(vX));
        vYPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(vY));
        vAzimuthPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(vAZ));

        aXPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(aX));
        aYPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(aY));
        aAzimuthPoly = new PolynomialFunction(PolynomialCurveFitter.create(5).fit(aAZ));
    }
}

/*
Port of python snippet attached below:

import numpy as np, numpy.polynomial.polynomial as P
import matplotlib.pyplot as plt
'''
This file will be used to help develop the navigation filter, which will improve sensor accuracy
This algorithm should have minimal latency
We will do the following steps:
 1. Get the current data (for now we will add noise to simulate sensor data)
 2. Try and fit the previous couple points to a polynomial curve
 3. Use the curve and the current acceleration to predict the next point
 4. Compare the predicted point to what it should have been
 5. Graph the filtered position, velocity, and acceleration, the position, velocity, and acceleration with/without
 noise, and the percent error of each
'''

'''
0<t<=3: a = 1
3<t<=6: a = 0
6<t<=9: a = -1

:returns (x, vX, aX, t)
'''
def generateActualData(t):
    accel = 1
    if t <= 3:
        a = accel
    elif t <= 6:
        a = 0
    else:
        a = -accel

    # To calculate position and velocity with a non-constant acceleration, we must use recursion
    if t <= 0:
        x = 0
        vX = 0
        return x, vX, a, t
    else:
        (x, vX, _, _) = generateActualData(t-0.1)
        vX += a*0.1
        x += vX*0.1

    return x, vX, a, t


'''
noise: gaussian
'''
def generateNoisyData(t):
    (_, _, aX, _) = generateActualData(t)
    aX += (np.random.normal(0.1, 2) * np.sign(np.random.randint(-1, 2)))
    vX = aX*t
    x = 0.5*aX*t**2
    return x, vX, aX, t


# Create a list of curves with linear lines
curves = [(0, P.polyfit([0], [0], 1), P.polyfit([0], [0], 1), P.polyfit([0], [0], 1))]

'''
This function will get every 10 points and fit a curve to them
Input: values: a list of tuples (x, vX, aX, t)
'''
def fitCurve(pos_history, interval=1000, createNew=True, t_step=0.01):
    latestValues = pos_history[-interval:]
    x = [v[0] for v in latestValues]
    vX = [v[1] for v in latestValues]
    aX = [v[2] for v in latestValues]
    t = [v[3] for v in latestValues]
    # Predict the next (interval/10) points, assuming the acceleration is constant, without noise filtering
    for i in range(0, int(interval/10)):
        t.append(t[-1] + t_step)
        vX.append(vX[-1] + aX[-1]*t_step)
        x.append(x[-1] + vX[-1]*t_step)
        aX.append(aX[-1])
    Xcurve = P.polyfit(t, x, 5)
    Vcurve = P.polyfit(t, vX, 5)
    AXCurve = P.polyfit(t, aX, 5)
    beginningTime = pos_history[0][3]
    if createNew:
        curves.append((beginningTime, Xcurve, Vcurve, AXCurve))
    else:
        curves[-1] = (beginningTime, Xcurve, Vcurve, AXCurve)


'''
This function will use the curve to predict the next point
'''
def predictNextPoint(pos_history, inputAccel, dT, t):
    # Get the most recent curve
    curve = curves[-1]
    # Get the time of the curve
    curveTime = curve[0]
    # Get the curve
    Xcurve = curve[1]
    Vcurve = curve[2]
    AXCurve = curve[3]
    # Now, we must use the acceleration to predict the next point
    # First, we must get the acceleration at the current time and compare it to the input acceleration

    # Get the acceleration at the current time
    aX = P.polyval(t, AXCurve)

    # Now, we must compare the two accelerations
    # We must correct the acceleration using the curve
    # We will use the average of the two accelerations
    aXCorrected = (aX + inputAccel)/2

    # Now, we must use the corrected acceleration to predict the next point
    vXPredicted = P.polyval(t, Vcurve)
    vXCalculated = P.polyval(t-dT, Vcurve) + aXCorrected*dT
    vXCorrected = (vXPredicted + vXCalculated)/2

    xPredicted = P.polyval(t, Xcurve)
    xCalculated = P.polyval(t-dT, Xcurve) + vXCorrected*dT
    xCorrected = (xPredicted + xCalculated)/2

    pos_history.append((xCorrected, vXCorrected, aXCorrected, t))
    if len(pos_history) % 1000 == 0:
        fitCurve(pos_history, 1000, True)
    else:
        fitCurve(pos_history, 1000, False)

    return pos_history, xCorrected, vXCorrected, aXCorrected, t

def graphAll():
    t = 30
    t_step = 0.01
    # History of values
    pos_history = []
    # Get the raw position data
    rawValues = []
    for i in range(0, int(t * (1/t_step))):
        rawValues.append(generateActualData((i+1)/100))
    # Get the noisy position data
    noisyValues = []
    for i in range(0, int(t * (1/t_step))):
        noisyValues.append(generateNoisyData((i+1)/100))
    # Get the filtered position data
    filteredValues = []
    for i in range(0, int(t * (1/t_step))):
        filtered = predictNextPoint(pos_history, noisyValues[i][2], 0.01, (i+1)/100)
        filteredValues.append((filtered[1], filtered[2], filtered[3], filtered[4]))
        pos_history = filtered[0]
    # Get the percent error
    percentError = []
    for i in range(0, int(t * (1/t_step))):
        percentError.append(abs((filteredValues[i][0] - rawValues[i][0])/rawValues[i][0]))
    # Graph the raw position data, the noisy position data, the filtered position data, and the percent error
    rawX = [v[0] for v in rawValues]
    t = [v[3] for v in rawValues]
    noisyX = [v[0] for v in noisyValues]
    filteredX = [v[0] for v in filteredValues]
    plt.plot(t, percentError, label='Percent Error')
    plt.legend()
    plt.show()
    plt.plot(t, rawX, label='Actual Position', color='green', linewidth=2)
    # plt.plot(t, noisyX, label='Noisy Position')
    plt.plot(t, filteredX, label='Filtered Position', color='red', linewidth=2)
    plt.legend()
    plt.show()
    # Graph the raw velocity data, the noisy velocity data, and the filtered velocity data
    rawVX = [v[1] for v in rawValues]
    noisyVX = [v[1] for v in noisyValues]
    filteredVX = [v[1] for v in filteredValues]
    plt.plot(t, rawVX, label='Actual Velocity', color='green', linewidth=2)
    # plt.plot(t, noisyVX, label='Noisy Velocity')
    plt.plot(t, filteredVX, label='Filtered Velocity', color='red', linewidth=2)
    plt.legend()
    plt.show()
    # Graph the raw acceleration data, the noisy acceleration data, and the filtered acceleration data
    rawAX = [v[2] for v in rawValues]
    noisyAX = [v[2] for v in noisyValues]
    filteredAX = [v[2] for v in filteredValues]
    plt.plot(t, rawAX, label='Actual Acceleration', color='green', linewidth=2)
    # plt.plot(t, noisyAX, label='Noisy Acceleration')
    plt.plot(t, filteredAX, label='Filtered Acceleration', color='red', linewidth=2)
    plt.legend()
    plt.show()


if __name__ == "__main__":
    graphAll()
 */