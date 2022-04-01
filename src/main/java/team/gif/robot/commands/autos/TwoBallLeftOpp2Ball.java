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

public class TwoBallLeftOpp2Ball extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-4.5, 0.0, 0.0)
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command oppOneBall() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-4.5, 0.0, 0.0),
                        new Pose2dFeet().set(-3.5, 4.0, 95.0)
                ),
                RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command oppTwoBall() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-3.5, 4.0, 95.0),
                        new Pose2dFeet().set(-1.5, 2, -90.0), // turn in place
                        new Pose2dFeet().set(-1.5, -9.0, -60.0)
                ),
                RobotTrajectory.getInstance().configReverse
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command oppTwoBallShoot() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-1.5, -9.0, -60.0),
                        new Pose2dFeet().set(-1.5, -6.0, 155.0) // turn in place
                ),
                RobotTrajectory.getInstance().configForwardFast
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public TwoBallLeftOpp2Ball() {

        addCommands(
            new CollectorDown(),
            new ParallelDeadlineGroup(
                new CollectorRun().withTimeout(2),
                reverse(),
                new HoodUp(),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RapidFire().withTimeout(2),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                oppOneBall(),
                new CollectorRun().withTimeout(4)
            ),
            new ParallelDeadlineGroup(
                oppTwoBall(),
                new CollectorRun()
            ),
            new ParallelDeadlineGroup(
                oppTwoBallShoot(),
                new CollectorRun()
            ),
            new ParallelDeadlineGroup(
                new RapidFire().withTimeout(3),
                new RevFlywheel(Constants.Shooter.RPM_FENDER_LOWER_HUB_BLOCKED)
            )
        );
    }
}
