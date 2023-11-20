package org.firstinspires.ftc.teamcode.internals.image.centerstage;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.features.SpikeMarkDetector;
import org.firstinspires.ftc.teamcode.features.VisionProcessingFeature;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class SpikeMarkTest extends OperationMode implements AutonomousOperation {

    VisionProcessingFeature detector;


    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

    @Override
    public void construct() {
        detector = new VisionProcessingFeature(new SpikeMarkDetectionPipeline());
        registerFeature(detector);
    }

    @Override
    public void run() {

    }
}
