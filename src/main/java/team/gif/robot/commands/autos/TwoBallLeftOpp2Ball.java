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
                new Pose2dFeet().set(-4.0, 0.0, 0.0)
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
                        new Pose2dFeet().set(-4.0, 0.0, 0.0),
                        new Pose2dFeet().set(-4.25, 3.5, 90.0)
                ),
                RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command oppTwoBallPart1() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-4.25, 3.5, 90.0),
                        new Pose2dFeet().set(-6.0,0,137) // 2 point turn
                ),
                RobotTrajectory.getInstance().configForward
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command oppTwoBall() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-6.0, 0, 137.0),
                        new Pose2dFeet().set(1.0, 0.0, -133), // Apex of turn
                        new Pose2dFeet().set(-0.4, -8.0, -43.0)
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
                        new Pose2dFeet().set(-0.4, -8.0, -43.0),
                        new Pose2dFeet().set(-3.0, -6.0, 156.0)
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
            new ParallelDeadlineGroup(
                new CollectorRun().withTimeout(2),
                new CollectorDown(),
                reverse(),
                new HoodUp(),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB-200)
            ),
            new ParallelDeadlineGroup(
                new RapidFire().withTimeout(1.6),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB-200)
            ),
            new ParallelDeadlineGroup(
                oppOneBall(),
                new CollectorRun()
            ),
            oppTwoBallPart1(), // drive forward and 2 point turn
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
