package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class ThreeBallTerminalMiddle extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-4.5, 0, 0.0) // 1st cargo location
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
//        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
        return rc;
    }

    public Command reverseAgain() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-4.5, 0, 0),
                new Pose2dFeet().set(-6.5, 0, -10) // shooting position
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command reverseAgainTwo() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-6.5, 0, -10),
                new Pose2dFeet().set(-17.0, 4, 0) // 2nd cargo (terminal) location
            ),
            RobotTrajectory.getInstance().configReverse
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-17.0, 4, 0),
                new Pose2dFeet().set(-6.0, 2, -2) // shooting position
            ),
            RobotTrajectory.getInstance().configForwardFast
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public ThreeBallTerminalMiddle() {
        addCommands(
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorDown(),
                new CollectorRun(),
                new HoodUp()
            ),
            new ParallelDeadlineGroup(
                reverseAgain(),
                new RevFlywheel(Constants.Shooter.RPM_AUTO_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_AUTO_UPPER_HUB).withTimeout(2),
                new RapidFire()
            ),
            new ParallelDeadlineGroup(
                reverseAgainTwo(),
                new CollectorRun()
            ),
            new CollectorRun().withTimeout(2),
            forward(),
            new ParallelDeadlineGroup(
                new CollectorRun().withTimeout(1.5),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB),
                new RapidFire()
            )
        );
    }
}
