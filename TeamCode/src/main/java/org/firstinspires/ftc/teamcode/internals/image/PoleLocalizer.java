package org.firstinspires.ftc.teamcode.internals.image;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Objects;

@Config
public class PoleLocalizer extends OpenCvPipeline {

    private final boolean indexed;
    private final int index;
    private long time = 0;

    public PoleLocalizer(int index) {
        indexed = true;
        this.index = index;
    }

    public PoleLocalizer() {
        indexed = false;
        index = 0;
    }

    public static int minR = 30, maxR = 70;
    public static double minC = 0, maxC = 1, alpha = 1, beta = 0, lasagna = 1, minDist = 100, dp = 1.2, blurSize = 3.0;

    private double poleDistanceX = 0, poleDistanceY = 0, poleDistance = 0;
    private boolean data = false;
    private int width = 0, height = 0; // Is set after first run of #processFrame(Mat), call that before accessing these!
    private int iteration = 0;

    @Override
    public Mat processFrame(Mat input) {

        width = input.width();
        height = input.height();

        // contrast & brightness
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY);
        input.convertTo(input, -1, alpha, beta);


        Size _blurSize = new Size(blurSize, blurSize);
        Imgproc.GaussianBlur(input, input, _blurSize, 0.0);

        // find circles
        Mat output = new Mat();
        Imgproc.HoughCircles(input, output, Imgproc.HOUGH_GRADIENT, dp, minDist);

        // draw for debugging
        for(int i = 0; i < output.cols(); i++) {
            double[] c = output.get(0, i);
            double r = c[2];
            if(r < minR || r > maxR) {
                continue;
            }
            Imgproc.circle(input, new Point(c[0], c[1]), (int) c[2], new Scalar(0, 255, 0), 2);
            Imgproc.rectangle(input, new Point(c[0], c[1]), new Point(c[0], c[1]), new Scalar(0, 255, 0), 5);
        }

        // Find the keypoint closest to the center of the image
        KeyPoint closestKPT = null;
        double closestKPT_dist = 1000000000; // Really big number
        Point center = new Point(output.width()/2.0, output.height()/2.0);
        for(int i = 0; i < output.cols(); i++) {
            double[] c = output.get(0, i);
            double x = c[0];
            double y = c[1];
            double r = c[2];
            if(r < minR || r > maxR) {
                continue;
            }
            Point p = new Point(x, y);
            double dist = Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2));
            if (dist < closestKPT_dist) {
                closestKPT = new KeyPoint((float) p.x, (float) p.y, (float) r * 2);
                closestKPT_dist = dist;
            }
        }

        // Put the distance from the center in a global static variable
        if(closestKPT != null) {
            double[] dists = findDistance(closestKPT, center);
            poleDistanceX = dists[0];
            poleDistanceY = dists[1];
            poleDistance = closestKPT_dist;
            data = true;
        }else{
            cache(iteration);
        }

        output.release();

        return input;
    }

    /**
     * @return The distance from the pole to the center of the camera in the X direction. No specific units whatsoever
     * Positive value means the pole is to the RIGHT of the camera, negative is LEFT
     */
    public double getPoleDistanceX() { return poleDistanceX; }

    /**
     * @return The distance from the pole to the center of the camera in the Y direction. No specific units whatsoever
     * Positive value means the pole is to the SOUTH of the camera, negative is NORTH
     */
    public double getPoleDistanceY() { return poleDistanceY; }

    /**
     * @return Get the overall distance from the center of the camera to the pole. Always positive.
     */
    public double getPoleDistance() { return poleDistance; }

    /**
     * @return An array containing the width and height (in that order) of the most recent {@link Mat} passed to {@link #processFrame(Mat)}.
     */
    public int[] getSize() { return new int[] { width, height }; }

    /**
     * @return An array containing the X, Y, width, and height (in that order) of the most recent frame processed.
     */
    public double[] getData() {
        if(data) {
            return new double[] { poleDistanceX, poleDistanceY, width, height };
        }else{
            return null;
        }
//        if(data) {
//            return new double[] { poleDistanceX, poleDistanceY, width, height, };
//        }else{
//            return null;
//        }
    }

    /**
     * Forcefully invalidate the cache. By default, the cache is invalidated every time new data is received or 1 second passes with no new data (in which case the cached data becomes filler). This method forces the cache to be invalidated and new data to be retrived, if possible.
     */
    public void invalidate() {
        data = false;
        iteration = 0;
    }

    private double[] findDistance(KeyPoint kpt, Point center) {
        return new double[] { (kpt.pt.x - center.x) / (Math.abs(kpt.size) * lasagna), (kpt.pt.y - center.y) / (Math.abs(kpt.size) * lasagna) };
    }

    private void cache(int iteration) {
        if(iteration == 0) {
            time = System.currentTimeMillis() + 1000;
        }
        if(System.currentTimeMillis() > time) {
            poleDistanceX = 4;
            poleDistanceY = 2;
            data = true;
        }
    }

    public void startStreaming() {
        System.out.println("we here");
        int cameraMonitorViewId = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getResources().getIdentifier("cameraMonitorViewId", "id", HardwareGetter.getHardwareMap().appContext.getPackageName());

        int[] viewportContainerIds = null;
        if(indexed) {
            viewportContainerIds = MultipleCameraManager.get(cameraMonitorViewId);
        }

        OpenCvCamera camera;

        System.out.println("viewport got");

        if(indexed) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera1, viewportContainerIds[index]);
        }else{
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera1, cameraMonitorViewId);
        }

        System.out.println("cam get");

        camera.setPipeline(this);
        FtcDashboard.getInstance().startCameraStream(camera, 0);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */

                AdvancedLogging.logData("Camera error", errorCode);
                System.out.println("Camera error: " + errorCode);
                AdvancedLogging.update();
            }
        });
    }

}


