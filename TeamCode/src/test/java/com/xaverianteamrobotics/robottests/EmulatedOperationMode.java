package com.xaverianteamrobotics.robottests;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import com.michaell.looping.builtin.ConvertToScript;

import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated.EmulatedGamepadRequest;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;

/**
 * Extend from this class to make an emulated OpMode
 * In order to run your emulated opmode, you can click the run icon next to the class name,
 * or in the tests list in Android Studio
 */
public abstract class EmulatedOperationMode {
    public ScriptParameters environment;

    private double timestamp = 0;
    private HashMap<String, Double> otherTimestamps = new HashMap<>();
    private double time;
    private double startMillis;
    private boolean stopRequested = false;

    private int timeUntillAbort = 30;

    public int getTimeUntillAbort() {
        return timeUntillAbort;
    }

    public void setTimeUntillAbort(int timeUntillAbort) {
        this.timeUntillAbort = timeUntillAbort;
    }

    public void waitForStart() {
        System.out.println("Opmode has finished initializing");
    }

    public void resetStartTime() {
        // This method is only used to maintain compatibility
        // It does not do anything
        startMillis = (double) System.currentTimeMillis();
    }

    public boolean opModeIsActive() {
        return !isStopRequested() & time < timeUntillAbort;
    }

    public boolean isStopRequested() {
        return stopRequested;
    }

    public void requestOpModeStop() {
        stopRequested = true;
    }

    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runOpMode() {
        startMillis = (double) System.currentTimeMillis();
        time = 0.0;
        ScriptTemplate jloopingScript;
        HardwareGetter.setEmulated(true);
        ScriptRunner runner = new ScriptRunner();

        try {
            jloopingScript = new ConvertToScript(this.getClass().getName(), this,
                "absolutelyNothing", "run");
            // run the opmode using jlooping
            runner.addScript(jloopingScript);
            this.environment = runner.scriptParametersGlobal;
            HardwareGetter.setJloopingRunner(runner);
            HardwareGetter.makeGamepadRequest("gamepad1", null);
            HardwareGetter.makeGamepadRequest("gamepad2", null);
            HardwareGetter.initStdDevices();
            // set environment variables
            // tell user-defined code of the opmode to construct itself
            construct();
            resetStartTime();
            // wait until the opmode is executed
            waitForStart();
            while (opModeIsActive()) {
                time = ((double) System.currentTimeMillis() - startMillis) / 1000;
                runner.runOneScript();
                this.environment = runner.scriptParametersGlobal;
                sleep(250);
            }
            // tell the app to stop this opmode
            assert(true);
        } catch (NoSuchMethodException | ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
            assert(false);
        }
    }

    /**
     * Updates the timestamp stored in the {@link OperationMode} to the current time.
     */
    public void updateDefaultTimestamp() {
        timestamp = time;
    }

    /**
     * Sets the data of an emulated gamepad
     * @param value The GamepadEmulatedValue class that contains the information to forward to the virtual gamepad
     * @param gamepadName The name of the emulated gamepad. "emulatedGamepad" will be added automatically to the beginning of the name, which is required by the EmulatedGamepadRequest
     * @throws ScriptParameters.VariableNotFoundException Thrown if there is no valid gamepad
     */
    public void setGamepadValues(EmulatedGamepadRequest.GamepadEmulatedValue value, String gamepadName) throws ScriptParameters.VariableNotFoundException {
        Objects.requireNonNull(HardwareGetter.getJloopingRunner()).scriptParametersGlobal.getGlobalVariable(gamepadName).setValue(value);
    }

    /**
     * Updates the timestamp stored in the {@link OperationMode} to a specific time.
     * @param time The time to set
     */
    public void updateDefaultTimestamp(double time) {
        timestamp = time;
    }

    /**
     * Gets the most recent timestamp set by {@link #updateDefaultTimestamp()} or {@link #updateDefaultTimestamp(double)}, or 0 if the timestamp has never been set.
     * @return The most recent timestamp
     */
    public double getDefaultTimestamp() {
        return timestamp;
    }

    /**
     * Updates a named timestamp to the current time. If a timestamp does not exist with this name, the timestamp is created.
     * @param name The name of the timestamp
     */
    public void updateNamedTimestamp(String name) {
        otherTimestamps.put(name, time);
    }

    /**
     * Updates a named timestamp to a specific time. If a timestamp does not exist with this name, the timestamp is created.
     * @param name The name of the timestamp
     * @param time The time to set
     */
    public void updateNamedTimestamp(String name, double time) {
        otherTimestamps.put(name, time);
    }

    /**
     * Gets the most recent timestamp with a certain name, or 0 if the timestamp has never been set.
     * @param name The name of the timestamp
     * @return The most recent timestamp with that name
     */
    public Double getNamedTimestamp(String name) {
        Double timestamp = otherTimestamps.get(name);
        if(timestamp == null) {
            timestamp = 0D;
        }
        return timestamp;
    }

    public abstract void construct();
    public abstract void run();

    public void absolutelyNothing() {
        // do absolutely nothing
    }
}
