package org.firstinspires.ftc.teamcode.internals.motion.auto_auto.tests;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.InitializeAutoAuto;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoPathSegment;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.BestPathFinder;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

import java.util.ArrayList;

public class BestPathFinderTestOpMode extends OperationMode implements TeleOperation {
	@Override
	public void construct() {
		InitializeAutoAuto.initialize();

		Logging.setAutoClear(false);
		Logging.log("BestPathFinderTest: Started");
		Logging.update();
		long time = System.currentTimeMillis();

		Logging.log(BestPathFinder.getFastestPathToPoint(new Vector2d(0, 0), new Vector2d(48, 36), 0).toString());

		Logging.log("BestPathFinderTest: Overall Time - " + (System.currentTimeMillis() - time) + "ms");
		Logging.update();
	}

	@Override
	public void run() {

	}
}
