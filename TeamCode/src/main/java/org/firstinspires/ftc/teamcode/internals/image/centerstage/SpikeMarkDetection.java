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
import static org.opencv.core.Core.FILLED;
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

    public static int MIN_AVG_AREA = 45;

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {

        // Convert to YCrCb
        Mat labColorSpace = new Mat();
        cvtColor(input, labColorSpace, COLOR_RGB2YCrCb);

        // Isolate the channels
        ArrayList<Mat> YCrCbChannels = new ArrayList<>();
        split(labColorSpace, YCrCbChannels);

        /*
         * Define the box surrounding where each position is
         * Zone 1: x1 = 100, x2 = 200, y1 = 240, y2 = 370
         * Zone 2: x1 = 400, x2 = 560, y1 = 230, y2 = 430
         */

        int channelOfInterest = isBlueTeam ? 2 : 1;
        Mat channel = YCrCbChannels.get(channelOfInterest);

        // Zone 1
        Rect zone1Rect = new Rect(50, 240, 100, 130);
        Mat zone1 = new Mat(channel, zone1Rect);

        // Zone 2
        Rect zone2Rect = new Rect(400, 230, 160, 200);
        Mat zone2 = new Mat(channel, zone2Rect);

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

        channel.release();
        zone1.release();
        zone2.release();
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
        isBlueTeam = teamColor == TeamColor.BLUE;
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
