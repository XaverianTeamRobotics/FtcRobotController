package com.xaverianteamrobotics.robottests;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.BestPathFinder;
import org.junit.Test;

import java.util.ArrayList;

public class BestPathFinderTest {
	@Test
	public void printAllPaths() {
		long time = System.currentTimeMillis();
		System.out.println("BestPathFinderTest: Started");
		AutoNoNavigationZones.addCenterstageDefaults();
		BestPathFinder.populate();

		for (ArrayList<AutoAutoPathSegment> path : BestPathFinder.getPathsToPoint(new Vector2d(0, 0), new Vector2d(48, 36))) {
			System.out.println(path);
		}
		System.out.println("BestPathFinderTest: Time taken - " + (System.currentTimeMillis() - time) + "ms");
	}
}
