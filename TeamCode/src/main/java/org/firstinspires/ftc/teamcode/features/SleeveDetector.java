package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.MultipleCameraManager;
import org.firstinspires.ftc.teamcode.internals.image.SleeveColorDetection;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.Objects;

public class SleeveDetector extends Feature implements Buildable {

    private SleeveColorDetection detector;

    private int spot = 0;
    private final ArrayList<Integer> previousSpots = new ArrayList<>();
    private int averageSpot = 0;
    private boolean init = false;
    private boolean indexed = false;
    private int index = 0;

    /**
     * Use this when only using one camera at a time.
     */
    public SleeveDetector() {}

    /**
     * Use this when two cameras are streaming concurrently.
     * @param index The index of the camera, either 0 or 1.
     */
    public SleeveDetector(int index) {
        indexed = true;
        this.index = index;
    }

    @Override
    public void build() {

        int cameraMonitorViewId = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getResources().getIdentifier("cameraMonitorViewId", "id", HardwareGetter.getHardwareMap().appContext.getPackageName());

        int[] viewportContainerIds = null;
        if(indexed) {
            viewportContainerIds = MultipleCameraManager.get(cameraMonitorViewId);
        }

        OpenCvCamera camera;

        if(indexed) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0, viewportContainerIds[index]);
        }else{
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0, cameraMonitorViewId);
        }

        detector = new SleeveColorDetection();

        detector.setDebugEnabled(false);

        camera.setPipeline(detector);
        FtcDashboard.getInstance().startCameraStream(camera, 0);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() {
                init = true;
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */

                AdvancedLogging.logData("Camera error", errorCode);
                AdvancedLogging.update();
            }
        });
    }

    @Override
    public void loop() {
        spot = detector.getDetection();
        previousSpots.add(spot);
        // Get the average spot out of all spots, then round it
        averageSpot = (int) Math.round(previousSpots.stream().mapToInt(Integer::intValue).average().orElse(0));
    }

    public int getSpot() {
        return spot;
    }

    public int getAverageSpot() {
        return averageSpot;
    }

    public boolean isReady() {
        return init;
    }

    public void setDebugEnabled(boolean enabled) {
        detector.setDebugEnabled(enabled);
    }

}
