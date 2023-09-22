package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public class EncoderConfig {

    public String NAME;
    public OdoEncoder.Direction DIRECTION;

    public EncoderConfig(String name, OdoEncoder.Direction direction) {
        NAME = name;
        DIRECTION = direction;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return NAME + " " + DIRECTION;
    }

}
