package org.firstinspires.ftc.teamcode.internals.image.centerstage;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.internals.image.VisionPipeline;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

import static org.opencv.core.Core.*;
import static org.opencv.features2d.Features2d.drawKeypoints;
import static org.opencv.imgproc.Imgproc.*;

@Config
public class SpikeMarkDetectionPipeline extends VisionPipeline {
    public static int ZONE1_X = 75;
    public static int ZONE1_Y = 300;
    public static int ZONE1_WIDTH = 175;
    public static int ZONE1_HEIGHT = 100;
    public static int ZONE2_X = 400;
    public static int ZONE2_Y = 330;
    public static int ZONE2_WIDTH = 240;
    public static int ZONE2_HEIGHT = 140;
    public static boolean isBlueTeam = true;
    public static int BLUE_THRESH = 150;
    public static int RED_THRESH = 150;
    public static int MIN_AVG_AREA = 40;

    private Mat labColorSpace;
    private Mat channel;
    private ArrayList<Mat> YCrCbChannels = new ArrayList<>();
    private Mat zone1;
    private Mat zone2;

    @Override
    public void init(Mat mat) {
        super.init(mat);
        labColorSpace = new Mat();
        channel = new Mat();
        zone1 = new Mat();
        zone2 = new Mat();
    }

    @Override
    public Mat processFrame(Mat input) {

        // Convert to YCrCb
		cvtColor(input, labColorSpace, COLOR_RGB2YCrCb);

        for (Mat channel : YCrCbChannels) {
            channel.release();
        }

        YCrCbChannels.clear();
        // Isolate the channels
        split(labColorSpace, YCrCbChannels);

        // Get the channel of interest (Cb for blue team, Cr for red team)
        int channelOfInterest = isBlueTeam ? 2 : 1;
		channel = YCrCbChannels.get(channelOfInterest);

		/*
         * Define the box surrounding where each position is
         * Zone 1: x1 = 100, x2 = 200, y1 = 240, y2 = 370
         * Zone 2: x1 = 400, x2 = 560, y1 = 230, y2 = 430
         */
        // Zone 1
        Rect zone1Rect = new Rect(ZONE1_X, ZONE1_Y, ZONE1_WIDTH, ZONE1_HEIGHT);

		zone1 = new Mat(channel, zone1Rect);

		// Zone 2
        Rect zone2Rect = new Rect(ZONE2_X, ZONE2_Y, ZONE2_WIDTH, ZONE2_HEIGHT);
		zone2 = new Mat(channel, zone2Rect);

		// Draw the rectangles
        rectangle(input, zone1Rect, new Scalar(255, 0, 0), 2);
        rectangle(input, zone2Rect, new Scalar(255, 0, 0), 2);

        // Threshold each zone
        int thresh;
        if (isBlueTeam) thresh = BLUE_THRESH;
        else thresh = RED_THRESH;

        inRange(zone1, new Scalar(thresh), new Scalar(255), zone1);
        inRange(zone2, new Scalar(thresh), new Scalar(255), zone2);

        // Get the average value of each zone
        Scalar zone1Avg = mean(zone1);
        Scalar zone2Avg = mean(zone2);

        Logging.log("Zone 1", zone1Avg.val[0]);
        Logging.log("Zone 2", zone2Avg.val[0]);
        Logging.update();

        // Determine the position based on the average value of each zone
        if (zone1Avg.val[0] < MIN_AVG_AREA && zone2Avg.val[0] < MIN_AVG_AREA) {
            position = 1;
        } else if (zone1Avg.val[0] > MIN_AVG_AREA) {
            position = 2;
        } else if (zone2Avg.val[0] > MIN_AVG_AREA) {
            position = 3;
        } else {
            position = 0;
        }

        // add the number of the color to the image
        Imgproc.putText(input, String.valueOf(position), new Point(10, 50), FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);
        return input;
    }

    @Override
    public void setTeamColor(TeamColor teamColor) {
        isBlueTeam = teamColor == TeamColor.BLUE;
        color = teamColor;
    }
}
