package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class Prod1 extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {
        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeBlueDark())
            .setConstraints(30, 30, 3, 3, 13.90)
            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(-58, 35, 0))
                    .splineTo(new Vector2d(-46, 35), 0)
                    .lineToSplineHeading(new Pose2d(-34, 35, Math.toRadians(90)))
                    .build());
    }

}
