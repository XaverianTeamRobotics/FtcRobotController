package org.firstinspires.ftc.teamcode.internals.image.centerstage;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.internals.image.powerplay.ImageProcessingConstants.*;
import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class SpikeMarkAndColorDetection extends OpenCvPipeline {
    private volatile int position = 0;
    private volatile boolean debugEnabled = false;
    private volatile TeamColor color;

    private int debugCrossX = 0, debugCrossY = 0;

    private final Scalar RED_HSV_MIN = new Scalar(0, 53, 73);
    private final Scalar RED_HSV_MAX = new Scalar(20, 255, 255);
    private final Scalar BLUE_HSV_MIN = new Scalar(100, 53, 73);
    private final Scalar BLUE_HSV_MAX = new Scalar(120, 255, 255);

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {
        // Blur both the image to reduce noise
        Size blurSize = new Size(GAUSSIAN_BLUR_SIZE, GAUSSIAN_BLUR_SIZE);
        GaussianBlur(input, input, blurSize, 0);

        cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        // Create a red mask
        Mat redMask = new Mat();
        inRange(input, RED_HSV_MIN, RED_HSV_MAX, redMask);

        // Create a blue mask
        Mat blueMask = new Mat();
        inRange(input, BLUE_HSV_MIN, BLUE_HSV_MAX, blueMask);

        if (debugEnabled) {
            bitwise_or(redMask, blueMask, redMask);
            cvtColor(redMask, redMask, COLOR_GRAY2RGB);
            cvtColor(input, input, COLOR_HSV2RGB);
            bitwise_and(input, redMask, input);
            cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

            double gamepadX = Devices.controller1.getLeftStickX();
            double gamepadY = Devices.controller1.getLeftStickY();

            debugCrossX += gamepadX / 25;
            debugCrossY += gamepadY / 25;

            debugCrossX = Math.min(debugCrossX, input.width());
            debugCrossY = Math.min(debugCrossY, input.height());

            debugCrossX = Math.max(debugCrossX, 0);
            debugCrossY = Math.max(debugCrossY, 0);

            double[] color = input.get(debugCrossX, debugCrossY);
            Imgproc.putText(
                    input,
                    Arrays.toString(color),
                    new Point(debugCrossX, debugCrossY),
                    FONT_HERSHEY_SIMPLEX, 0.5,
                    new Scalar(255, 255, 255));
            Imgproc.line(input,
                    new Point(debugCrossX, debugCrossY + 50),
                    new Point(debugCrossX, debugCrossY - 50),
                    new Scalar(255, 255, 255));
            Imgproc.line(input,
                    new Point(debugCrossX + 50, debugCrossY),
                    new Point(debugCrossX - 50, debugCrossY),
                    new Scalar(255, 255, 255));
        }

        redMask.release();
        blueMask.release();
        cvtColor(input, input, COLOR_HSV2RGB); // Convert back to RGB for display
        printOutcome();
        // add the number of the color to the image
        Imgproc.putText(input, String.valueOf(position), new Point(10, 50), FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);
        return input;
    }

    private void printOutcome() {
        if (debugEnabled) {
            switch (position) {
                case 1:
                    Logging.logData("Detected Color", "Magenta");
                    break;
                case 2:
                    Logging.logData("Detected Color", "Orange");
                    break;
                case 3:
                    Logging.logData("Detected Color", "Green");
                    break;
                default:
                    Logging.logData("Detected Color", "None");
                    break;
            }
            Logging.update();
        }
    }

    /**
     * Processes the image for a specific color
     * @param input The image to process. In HSV Color Space
     * @param lowHSV The lower bound of the color to detect
     * @param highHSV The upper bound of the color to detect
     * @param name The name to print on the telemetry
     * @return The average value of the color in the image
     */
    public double processForColor(Mat input, Scalar lowHSV, Scalar highHSV, String name) {
        Mat mat = new Mat();
        inRange(input, lowHSV, highHSV, mat);

        double sum = sumElems(mat).val[0];
        double area = mat.rows() * mat.cols();

        double averageValue = sum / area / 255;

        // Log all values used in calculation for verification purposes
        if (debugEnabled) {
            Logging.logData(name + " - Average", averageValue * 100 + "%");
            Logging.update();
        }

        mat.release();

        return averageValue;
    }

    public int getPosition() {
        return position;
    }
    public TeamColor getTeamColor() { return color; }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public enum TeamColor {
        BLUE, RED
    }
}
