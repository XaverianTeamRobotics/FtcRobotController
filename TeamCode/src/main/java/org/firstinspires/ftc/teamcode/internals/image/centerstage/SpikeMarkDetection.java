package org.firstinspires.ftc.teamcode.internals.image.centerstage;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.internals.image.powerplay.ImageProcessingConstants.*;
import static org.opencv.core.Core.*;
import static org.opencv.features2d.Features2d.drawKeypoints;
import static org.opencv.imgproc.Imgproc.*;

@Config
public class SpikeMarkDetection extends OpenCvPipeline {
    private volatile int position = 0;
    private volatile boolean debugEnabled = false;
    private static boolean isBlueTeam = true;
    public static boolean showYCrCb = false;
    public static int YCrCbPreviewChannel = 0;
    public static int BLUE_THRESH = 150;
    public static int RED_THRESH = 150;

    /**
     * The right bound of the vertical zone for target 1
     * -1 for not in screen.
     * Both VERT_ZONE_! and VERT_ZONE_3 cannot equal -1
     */
    public static int VERT_ZONE_1 = -1;
    /**
     * The right bound of the vertical zone for target 2
     * The left bound is VERT_ZONE_!, or zero if VERT_ZONE_1 is negative
     * Cannot be -1.
     */
    public static int VERT_ZONE_2 = 400;
    /**
     * The right bound of the vertical zone for target 3
     * The left bound is VERT_ZONE_2
     * -1 for not in screen.
     * Both VERT_ZONE_! and VERT_ZONE_3 cannot equal -1
     */
    public static int VERT_ZONE_3 = 640;

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {
        // Blur both the image to reduce noise
        Size blurSize = new Size(GAUSSIAN_BLUR_SIZE, GAUSSIAN_BLUR_SIZE);
        GaussianBlur(input, input, blurSize, 0);

        // Convert to YCrCb
        Mat labColorSpace = new Mat();
        cvtColor(input, labColorSpace, COLOR_RGB2YCrCb);

        // Isolate the channels
        List<Mat> YCrCbChannels = new ArrayList<>();
        split(labColorSpace, YCrCbChannels);
        Mat redChannel = YCrCbChannels.get(1);
        Mat blueChannel = YCrCbChannels.get(2);

        // Threshold each channel
        Mat redThresh = new Mat();
        Mat blueThresh = new Mat();
        inRange(redChannel, new Scalar(RED_THRESH), new Scalar(255), redThresh);
        inRange(blueChannel, new Scalar(BLUE_THRESH), new Scalar(255), blueThresh);

        // Apply the threshold masks to the original image
        bitwise_and(redChannel, redThresh, redChannel);
        bitwise_and(blueChannel, blueThresh, blueChannel);
        redThresh.release();
        blueThresh.release();

        // Establish a mask for each vertical zone
        Mat zone1 = new Mat(input.rows(), input.cols(), CvType.CV_8UC1);
        Mat zone2 = new Mat(input.rows(), input.cols(), CvType.CV_8UC1);
        Mat zone3 = new Mat(input.rows(), input.cols(), CvType.CV_8UC1);
        if (VERT_ZONE_1 != -1) {
            zone1.colRange(0, VERT_ZONE_1).setTo(new Scalar(255));
        }
        zone2.colRange((VERT_ZONE_1 == -1) ? 0 : VERT_ZONE_1, VERT_ZONE_2).setTo(new Scalar(255));
        if (VERT_ZONE_3 != -1) {
            zone3.colRange(VERT_ZONE_2, VERT_ZONE_3).setTo(new Scalar(255));
        }
        if (VERT_ZONE_2 == -1) {
            OperationMode.emergencyStop("SpikeMarkDetection: VERT_ZONE_2 cannot be -1");
        }
        if (VERT_ZONE_1 == -1 && VERT_ZONE_3 == -1) {
            OperationMode.emergencyStop("SpikeMarkDetection: VERT_ZONE_1 and VERT_ZONE_3 cannot both be -1");
        }

        // Draw the vertical bars on the input image
        if (VERT_ZONE_1 != -1) line(input, new Point(VERT_ZONE_1, 0), new Point(VERT_ZONE_1, input.rows()), new Scalar(255, 0, 0), 2);
        line(input, new Point(VERT_ZONE_2, 0), new Point(VERT_ZONE_2, input.rows()), new Scalar(0, 255, 0), 2);
        if (VERT_ZONE_3 != -1) line(input, new Point(VERT_ZONE_3, 0), new Point(VERT_ZONE_3, input.rows()), new Scalar(0, 0, 255), 2);

        int channelOfInterest = isBlueTeam ? 2 : 1;

        bitwise_and(zone1, YCrCbChannels.get(channelOfInterest), zone1);
        bitwise_and(zone2, YCrCbChannels.get(channelOfInterest), zone2);
        bitwise_and(zone3, YCrCbChannels.get(channelOfInterest), zone3);

        // Get the average value of each zone
        Scalar zone1Avg = mean(zone1);
        Scalar zone2Avg = mean(zone2);
        Scalar zone3Avg = mean(zone3);

        Logging.log("Zone 1", zone1Avg.val[0]);
        Logging.log("Zone 2", zone2Avg.val[0]);
        Logging.log("Zone 3", zone3Avg.val[0]);
        Logging.update();

        if (showYCrCb) {
            // Set input to the YCrCb color space
            // Using the channel specified by YCrCbPreviewChannel
            input = YCrCbChannels.get(YCrCbPreviewChannel);
        }
        // add the number of the color to the image
        Imgproc.putText(input, String.valueOf(position), new Point(10, 50), FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);
        return input;
    }

    public int getPosition() {
        return position;
    }
    public TeamColor getTeamColor() {
        if (isBlueTeam) return TeamColor.BLUE;
        else return TeamColor.RED;
    }

    public void setTeamColor(TeamColor teamColor) {
        if (teamColor == TeamColor.BLUE) isBlueTeam = true;
        else isBlueTeam = false;
    }

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
