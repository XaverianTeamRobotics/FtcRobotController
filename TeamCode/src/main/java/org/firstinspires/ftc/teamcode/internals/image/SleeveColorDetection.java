package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.*;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.sumElems;
import static org.opencv.imgproc.Imgproc.*;

public class SleeveColorDetection extends OpenCvPipeline {
    private volatile int detection = 0;
    private volatile boolean debugEnabled = false;

    private int debugCrossX = 0, debugCrossY = 0;

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {
        // Blur both the image to reduce noise
        Size blurSize = new Size(GAUSSIAN_BLUR_SIZE, GAUSSIAN_BLUR_SIZE);
        GaussianBlur(input, input, blurSize, 0);

        cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        // Define the colors needed
        Scalar magentaLowHSV = new Scalar(MAGENTA_H_MIN, MAGENTA_S_MIN, MAGENTA_V_MIN);
        Scalar magentaHighHSV = new Scalar(MAGENTA_H_MAX, MAGENTA_S_MAX, MAGENTA_V_MAX);
        
        Scalar orangeLowHSV = new Scalar(ORANGE_H_MIN, ORANGE_S_MIN, ORANGE_V_MIN);
        Scalar orangeHighHSV = new Scalar(ORANGE_H_MAX, ORANGE_S_MAX, ORANGE_V_MAX);
        
        Scalar greenLowHSV = new Scalar(GREEN2_H_MIN, GREEN2_S_MIN, GREEN2_V_MIN);
        Scalar greenHighHSV = new Scalar(GREEN2_H_MAX, GREEN2_S_MAX, GREEN2_V_MAX);

        // Find the average colors of the original image
        double magentaAverage = processForColor(input, magentaLowHSV, magentaHighHSV, "magenta");
        double orangeAverage = processForColor(input, orangeLowHSV, orangeHighHSV, "orange");
        double greenAverage = processForColor(input, greenLowHSV, greenHighHSV, "green");

        double largestVal = Math.max(magentaAverage, Math.max(orangeAverage, greenAverage));
        if (largestVal == magentaAverage) {
            detection = 1;
        } else if (largestVal == orangeAverage) {
            detection = 2;
        } else if (largestVal == greenAverage) {
            detection = 3;
        } else {
            detection = 0;
        }

        // If debuggind is enabled, add a cross to the image where the color of the point can be retrieved
        if (debugEnabled) {
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
                    FONT_HERSHEY_SIMPLEX, 2,
                    new Scalar(255, 255, 255));
        }

        cvtColor(input, input, COLOR_HSV2RGB); // Convert back to RGB for display
        printOutcome();
        // add the number of the color to the image
        Imgproc.putText(input, String.valueOf(detection), new Point(10, 50), FONT_HERSHEY_SIMPLEX, 2, new Scalar(255, 255, 255), 2);
        return input;
    }

    private void printOutcome() {
        if (debugEnabled) {
            switch (detection) {
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

    public int getDetection() {
        return detection;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
