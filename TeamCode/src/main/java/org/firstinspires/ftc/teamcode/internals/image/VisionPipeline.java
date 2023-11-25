package org.firstinspires.ftc.teamcode.internals.image;

import org.openftc.easyopencv.OpenCvPipeline;

public abstract class VisionPipeline extends OpenCvPipeline {
    protected volatile int position = 0;
    protected volatile TeamColor color;
    protected volatile boolean debugEnabled = false;

    public int getPosition() {
        return position;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public TeamColor getTeamColor() { return color; }
    public void setTeamColor(TeamColor color) {
        this.color = color;
    }

    public enum TeamColor {
        BLUE, RED
    }
}
