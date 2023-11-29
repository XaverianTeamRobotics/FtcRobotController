package com.xaverianteamrobotics.robottests;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.geometry.Line;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones;
import org.junit.Test;

public class TestNoNavZones {
    @Test
    public void testNoNavZones() {
        AutoNoNavigationZones.addCenterstageDefaults();
        Line l1 = new Line(new Vector2d(-30, 60), new Vector2d(14, 60));
        System.out.println(l1.getSlope());
        System.out.println(l1.getFunction().evaluate(0));

        assert AutoNoNavigationZones.isIntersecting(l1);
    }
}
