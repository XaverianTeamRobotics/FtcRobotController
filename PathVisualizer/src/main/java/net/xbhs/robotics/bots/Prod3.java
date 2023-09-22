package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class Prod3 extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {

        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeBlueDark())
            .setConstraints(30, 30, 3, 3, 13.90)
            .setDimensions(14, 18)
            .followTrajectorySequence(drive -> {
                TrajectorySequence Three = drive.trajectorySequenceBuilder(new Pose2d(-35.84, 61.50, Math.toRadians(-90.00)))
                    // init hand (closes), init cam
                    // cam detect
//                    .splineTo(new Vector2d(35.84, 59.00), Math.toRadians(-90.00))
                    // arm from floor to reset
                    // arm from reset to jnct high
                    .splineToConstantHeading(new Vector2d(-35.14, 44.05), Math.toRadians(-90.00))
                    .splineToConstantHeading(new Vector2d(-35.14, 30.00), Math.toRadians(-90.00))
                    .splineTo(new Vector2d(30.58, 6.38), Math.toRadians(221.32))
                    .waitSeconds(0.1)
                    // hand open
                    // arm from jnct high to cone high
                    .lineToSplineHeading(new Pose2d(38.92, 11.11, Math.toRadians(0.11)))
                    .splineToConstantHeading(new Vector2d(57.52, 9.55), Math.toRadians(2.46))
                    .waitSeconds(0.1)
                    // hand close
                    // arm from cone high to jnct high
                    // wait 500ms
                    .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
                    .splineToConstantHeading(new Vector2d(28.55, 7.35), Math.toRadians(232.36))
                    .waitSeconds(0.1)
                    // hand open
                    // arm from jnct high to cone high
                    .lineToSplineHeading(new Pose2d(37.68, 9.26, Math.toRadians(1.30)))
                    .splineTo(new Vector2d(57.46, 9.13), Math.toRadians(359.18))
                    .waitSeconds(0.1)
                    // hand close
                    // arm from cone high to jnct high
                    // wait 500ms
                    .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
                    .splineToConstantHeading(new Vector2d(28.98, 5.97), Math.toRadians(228.78))
                    .waitSeconds(0.1)
                    // hand open
                    // arm from jnct high to reset
                    .lineToSplineHeading(new Pose2d(34.28, 11.20, Math.toRadians(267.34))) // two
                    .lineToConstantHeading(new Vector2d(57.77, 8.17)) // one
                    .turn(Math.toRadians(90.00))
                    .lineToSplineHeading(new Pose2d(11.21, 17.25, Math.toRadians(258.89))) // three pt.1
                    .lineTo(new Vector2d(13.96, 35.43)) // three pt.2
                    .build();
                return Three;
            });
    }

}
