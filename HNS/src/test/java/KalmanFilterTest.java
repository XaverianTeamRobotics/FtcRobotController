import net.xbhs.robotics.HNS.filters.RobotKalmanFilter;
import net.xbhs.robotics.HNS.Localizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class KalmanFilterTest {

    private static double[] results = new double[4];
    private static boolean printResults = true;

    // Create a test case to print the results of the RobotKalmanFilter
    // Each pose will have an accelration of our maxAccel (which is a public variable in the RobotKalmanFilter)
    // We will set the time between ticks to be RobotKalmanFilter.dt and we will limit ourselves to 100 ticks
    // We will also calculate the velocity and position of the robot based on the acceleration WITHOUT the noise to validate our results
    // Note we will only test the x axis, not the y and azimuth
    @Test
    public void testKalmanFilter() {
        // Create a KalmanFilter
        RobotKalmanFilter kalmanFilter = new RobotKalmanFilter(new Localizer());

        // Find the number of ticks to get 10 seconds
        int ticks = (int) (30 / kalmanFilter.dt);

        // Estimate the expected final position of the robot
        double accel = kalmanFilter.maxAccel;
        double expectedVX = 0;
        double expectedX = 0;

        // Run the test
        Localizer pos = new Localizer();

        boolean isSlowingDown = false;

        for (int i = 0; i < ticks; i++) {
            // Update the expected values
            expectedX = expectedX + expectedVX * kalmanFilter.dt + 0.5 * accel * kalmanFilter.dt * kalmanFilter.dt;
            expectedVX = expectedVX + accel * kalmanFilter.dt;

            // If velocity is greater than max velocity, set accel to 0
            if (pos.vX >= 0.762 && !isSlowingDown) {
                accel = -1/2 * kalmanFilter.maxAccel;
                isSlowingDown = true;
            }

            // If velocity is at or less than 0, set accel to max accel
            if (pos.vX <= 0 && isSlowingDown) {
                accel = kalmanFilter.maxAccel;
                isSlowingDown = false;
            }

            // Add noise to the acceleration between -0.2 and 0.2
            double noise = Math.random() * 0.4 - 0.2;
            // Update the KalmanFilter
            double noisyAccel = accel + noise;
            //double noisyAccel = accel;

            // Calculate our x and vX
            double vX = pos.vX + noisyAccel * kalmanFilter.dt;
            double x = pos.x + pos.vX * kalmanFilter.dt + 0.5 * noisyAccel * kalmanFilter.dt * kalmanFilter.dt;

            // Update our pos to be the new values
            pos.setPose(x, pos.y, pos.azimuth, vX, pos.vY, pos.vAzimuth);
            kalmanFilter.update(pos, kalmanFilter.dt);

            // Correct the KalmanFilter
            pos = kalmanFilter.correct();
        }

        if (printResults) {
            // Print the results
            System.out.println("Expected X: " + expectedX);
            System.out.println("Filtered X: " + pos.x);
            System.out.println("Expected VX: " + expectedVX);
            System.out.println("Filtered VX: " + pos.vX);
        }

        // Save the results
        results[0] = expectedX;
        results[1] = pos.x;
        results[2] = expectedVX;
        results[3] = pos.vX;
    }

    @Test
    public void multipleKalmanFilterTests() {
        printResults = false;
        ArrayList<double[]> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testKalmanFilter();
            results.add(Arrays.copyOf(KalmanFilterTest.results, KalmanFilterTest.results.length));
        }
        printResults = true;

        // Average the expected x
        double expectedX = 0;
        for (double[] result : results) {
            expectedX += result[0];
        }
        expectedX /= results.size();

        // Average the filtered x
        double filteredX = 0;
        for (double[] result : results) {
            filteredX += result[1];
        }
        filteredX /= results.size();

        // Average the expected vx
        double expectedVX = 0;
        for (double[] result : results) {
            expectedVX += result[2];
        }
        expectedVX /= results.size();

        // Average the filtered vx
        double filteredVX = 0;
        for (double[] result : results) {
            filteredVX += result[3];
        }
        filteredVX /= results.size();

        // Find the average difference between the expected and filtered x
        double avgDiffX = 0;
        for (double[] result : results) {
            avgDiffX += Math.abs(result[0] - result[1]);
        }
        avgDiffX /= results.size();

        // Find the average difference between the expected and filtered vx
        double avgDiffVX = 0;
        for (double[] result : results) {
            avgDiffVX += Math.abs(result[2] - result[3]);
        }
        avgDiffVX /= results.size();

        // Find the average percent error between the expected and filtered x
        double avgPercentErrorX = 0;
        for (double[] result : results) {
            avgPercentErrorX += Math.abs((result[0] - result[1]) / result[0]);
        }
        avgPercentErrorX /= results.size();

        // Find the average percent error between the expected and filtered vx
        double avgPercentErrorVX = 0;
        for (double[] result : results) {
            avgPercentErrorVX += Math.abs((result[2] - result[3]) / result[2]);
        }
        avgPercentErrorVX /= results.size();

        // Print the results
        System.out.println("Avg. Expected X: " + expectedX);
        System.out.println("Avg. Filtered X: " + filteredX);
        System.out.println("Avg. Expected VX: " + expectedVX);
        System.out.println("Avg. Filtered VX: " + filteredVX);
        System.out.println("Avg. Diff. X: " + avgDiffX);
        System.out.println("Avg. Diff. VX: " + avgDiffVX);
        System.out.println("Avg. Percent Error X: " + avgPercentErrorX * 100 + "%");
        System.out.println("Avg. Percent Error VX: " + avgPercentErrorVX * 100 + "%");
    }
}
