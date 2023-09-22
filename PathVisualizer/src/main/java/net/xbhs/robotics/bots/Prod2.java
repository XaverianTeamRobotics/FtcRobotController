package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class Prod2 extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {
        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeRedDark())
            .setConstraints(30, 30, 3, 3, 13.90)
            .setDimensions(14, 18)
            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(-35.84, 61.50, Math.toRadians(-90.00)))
                .splineToConstantHeading(new Vector2d(-35.50, 43.93), Math.toRadians(270.65))
                .splineToConstantHeading(new Vector2d(-35.00, 11.70), Math.toRadians(270.45))
                .splineTo(new Vector2d(-38.01, 10.30), Math.toRadians(177.99)) // two
                .lineTo(new Vector2d(-14.21, 8.28)) // one
                .lineTo(new Vector2d(-59.33, 11.27)) // three
                .build());
    }

}
