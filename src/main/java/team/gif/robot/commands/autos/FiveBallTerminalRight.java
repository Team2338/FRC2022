package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.*;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.collector.CollectorUp;
import team.gif.robot.commands.hood.HoodDown;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class FiveBallTerminalRight extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-3.4, 0.0, 0.0) //4.5 original
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command pickUpNext() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-3.4, 0.0, 0.0),
                new Pose2dFeet().set(-3.4, 2.0, 95.0), // ~turn in place
                new Pose2dFeet().set(-2.5, 8.0, 20.0) // 1st cargo location
            ),
            RobotTrajectory.getInstance().configReverse
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }
    public Command pickupTerminal() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-1.5, 8.0, 15.0),
                new Pose2dFeet().set(-2.5,21,45.0) // 2nd cargo (terminal) location
            ),
            RobotTrajectory.getInstance().configReverseFast
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-5.5, 20.5, 54.0),
                new Pose2dFeet().set(0.0, 11.0, 75.0) // shooting location
            ),
            RobotTrajectory.getInstance().configForwardFast
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }


    public FiveBallTerminalRight() {

        addCommands(
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorDown(),
                new CollectorRun(),
                new HoodUp(),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB).withTimeout(1.2),
                new CollectorRun().withTimeout(1).andThen(new CollectorUp()),
                new RapidFire()
            ),
            new ParallelDeadlineGroup(
                pickUpNext(),
                new WaitCommand(0.5).andThen(new CollectorDown()),
                new WaitCommand(1.5).andThen(new CollectorRun())
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB).withTimeout(1.1),
                new HoodUp(),
                new CollectorRun().withTimeout(1),
                new RapidFire()
            ),
            new ParallelDeadlineGroup(
                pickupTerminal(),
                new WaitCommand(2.5).andThen(new CollectorRun())
            ),
            new ParallelDeadlineGroup(
                forward(),
                new CollectorRun().withTimeout(2),
                new WaitCommand(3).andThen( new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB))
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB),
                new RapidFire()
            )
        );
    }
}
