package com.xaverianteamrobotics.robottests;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.math.geometry.Line;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones;
import org.junit.Test;

public class TestAutoNoNavZones {
    @Test
    public void testAutoNoNavZones() {
        long time = System.currentTimeMillis();
        AutoNoNavigationZones.addCenterstageDefaults();

        Line line1 = new Line(new Vector2d(0, -24), new Vector2d(4, 24));
        Line line2 = new Line(new Vector2d(40, -24), new Vector2d(44, 0));
        Line line3 = new Line(new Vector2d(20, -24), new Vector2d(21, 24));
        Line line4 = new Line(new Vector2d(40, -24), new Vector2d(40, 24));

        assert !AutoNoNavigationZones.isIntersecting(line1, false);
        assert AutoNoNavigationZones.isIntersecting(line2, false);
        assert AutoNoNavigationZones.isIntersecting(line3, true);
        assert AutoNoNavigationZones.isIntersecting(line4, true);
        System.out.println("TestAutoNoNavZones: " + (System.currentTimeMillis() - time) + "ms");
    }
}
