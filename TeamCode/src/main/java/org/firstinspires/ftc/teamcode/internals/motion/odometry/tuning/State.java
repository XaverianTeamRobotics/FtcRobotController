package org.firstinspires.ftc.teamcode.internals.motion.odometry.tuning;

import org.firstinspires.ftc.teamcode.internals.misc.Affair;

public class State {

    // tuning flow -- these values should be set to PRESENT in order as the user goes through these steps

    public static Affair beginningDirections = Affair.PRESENT;
    public static Affair constantDirections = Affair.FUTURE;
    public static Affair motorConfigSetup = Affair.FUTURE;
    public static Affair rpmTuning = Affair.FUTURE;
    public static Affair tickTuning = Affair.FUTURE;
    public static Affair wheelRadiusTuning = Affair.FUTURE;
    public static Affair gearRatioTuning = Affair.FUTURE;
    public static Affair trackWidthTuningEstimate = Affair.FUTURE;
    public static Affair constraintsEstimateTuning = Affair.FUTURE;
    public static Affair encoderTickTuning = Affair.FUTURE;
    public static Affair encoderWheelRadiusTuning = Affair.FUTURE;
    public static Affair encoderGearRatioTuning = Affair.FUTURE;
    public static Affair encoderTrackWidthTuning = Affair.FUTURE;
    public static Affair encoderForwardOffsetTuning = Affair.FUTURE;
    public static Affair beginPhysicalTuning = Affair.FUTURE;
    public static Affair maxVelocityTuner = Affair.FUTURE;
    public static Affair encoderTrackWidthExperimentalTuner = Affair.FUTURE;
    public static Affair encoderForwardOffsetExperimentalTuner = Affair.FUTURE;
    public static Affair autoFeedforwardTuner = Affair.FUTURE;
    public static Affair manualFeedforwardTuner = Affair.FUTURE;
    public static Affair lateralMultiplierTuning = Affair.FUTURE;
    public static Affair driveTrackWidthExperimentalTuning = Affair.FUTURE;
    public static Affair manualDriveTrackWidthExperimentalTuning = Affair.FUTURE;
    public static Affair followerTuning = Affair.FUTURE;
    public static Affair spline = Affair.FUTURE;
    public static Affair endTuning = Affair.FUTURE;

    // misc state -- an unordered mess of what we need
    public static OdometryTuner.InitialTesting initialTesting = OdometryTuner.InitialTesting.BEFORE;
    public static boolean firstTime = true;
    public static OdometryTuner.EndOfTuning endOfTuning = OdometryTuner.EndOfTuning.SAVE;

}
