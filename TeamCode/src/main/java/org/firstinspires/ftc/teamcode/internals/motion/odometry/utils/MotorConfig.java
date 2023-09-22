package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.jetbrains.annotations.NotNull;

public class MotorConfig {

    public String NAME;
    public DcMotorSimple.Direction DIRECTION;

    public MotorConfig(String name, DcMotorSimple.Direction direction) {
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
