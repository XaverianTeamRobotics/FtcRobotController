package org.firstinspires.ftc.teamcode.internals.motion;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoAutoCreator;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.AutoNoNavigationZones;
import org.firstinspires.ftc.teamcode.internals.motion.auto_auto.BestPathFinder;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

public class InitializeAutoAuto extends OperationMode implements AutonomousOperation {
	@Override
	public Class<? extends OperationMode> getNext() {
		return AutoAutoCreator.class;
	}

	@Override
	public void construct() {
		Logging.setAutoClear(false);
		Logging.clear();
		long time = System.currentTimeMillis();

		Logging.log("Initializing Auto Auto - Started");
		Logging.update();

		AutoNoNavigationZones.addCenterstageDefaults();
		BestPathFinder.populate();

		Logging.log("\nAuto Auto Initialized: Overall Time - " + ((int) ((System.currentTimeMillis() - time)/1000)) + "s");
		Logging.log("------------------------------");
		Logging.log("You can now stop this OpMode");
		Logging.update();

		Logging.setAutoClear(true);
	}

	@Override
	public void run() {
	}
}
