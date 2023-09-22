package org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;

import java.util.Objects;
import java.util.Queue;

public class AutoRunner {

    private final Auto auto, one, two, three;
    private final Queue<DriveCommand> driveCommands;
    private final AutonomousDrivetrain drivetrain;
    private DriveCommand currentDriveCommand = null;
    private boolean killed = false;

    public AutoRunner(Auto auto, AutonomousDrivetrain drivetrain, Auto one, Auto two, Auto three) {
        this.auto = auto;
        driveCommands = this.auto.path();
        this.drivetrain = drivetrain;
        this.one = one;
        this.two = two;
        this.three = three;
        this.currentDriveCommand = driveCommands.poll();
    }

    public void processSleeve(int result) {
        beeeeekaaaayhaveityourway(result);
    }

    private void beeeeekaaaayhaveityourway(int spot) {
        switch(spot) {
            case 1:
                driveCommands.addAll(one.path());
                driveCommands.add(new DriveCommand.Kill());
                break;
            case 2:
                driveCommands.addAll(two.path());
                driveCommands.add(new DriveCommand.Kill());
                break;
            case 3:
                driveCommands.addAll(three.path());
                driveCommands.add(new DriveCommand.Kill());
        }
    }

    public void run() {
        if(!killed) {
            if(!drivetrain.isBusy() && Objects.requireNonNull(HardwareGetter.getOpMode()).opModeIsActive()) {
                try {
                    DriveCommand.Drive command = (DriveCommand.Drive) currentDriveCommand;
                    drivetrain.followTrajectorySequenceAsync(command.getSequence());
                    currentDriveCommand = driveCommands.poll();
                } catch(ClassCastException e) {
                    try {
                        DriveCommand.Do command = (DriveCommand.Do) currentDriveCommand;
                        command.getAction().run();
                        currentDriveCommand = driveCommands.poll();
                    } catch(ClassCastException ex) {
                        try {
                            DriveCommand.Wait command = (DriveCommand.Wait) currentDriveCommand;
                            if(command.getSupplier().get()) {
                                currentDriveCommand = driveCommands.poll();
                            }
                        } catch(ClassCastException exc) {
                            killed = true;
                            drivetrain.setWeightedDrivePower(new Pose2d(0, 0, 0));
                            return;
                        }
                    }
                }
            }
            drivetrain.update();
        }
    }

}
