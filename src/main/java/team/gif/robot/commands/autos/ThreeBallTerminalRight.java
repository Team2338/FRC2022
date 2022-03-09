package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.*;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class ThreeBallTerminalRight extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(0.0, -0.2, 180.0)
                //new Pose2d(Units.feetToMeters(0.0), 0, new Rotation2d(0)),
                //new Pose2d(Units.feetToMeters(-3.0), 0, new Rotation2d(0))
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }
    public Command reverseAgain() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, -0.2, 180),
                new Pose2dFeet().set(0.0, -0.3, 360)
                //new Pose2d(Units.feetToMeters(0.0), 0, new Rotation2d(0)),
                //new Pose2d(Units.feetToMeters(-3.0), 0, new Rotation2d(0))
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public ThreeBallTerminalRight() {
        System.out.println("Auto: Mobility Selected");

        addCommands(
            new PrintCommand("Auto: Mobility Started"),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new ParallelCommandGroup(
                        reverse(),
                        new HoodUp(),
                        new CollectorRun().withTimeout(3)
                    ),
                    //align(),
                    new RapidFire().withTimeout(3)
                ),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new ParallelCommandGroup(
                        reverseAgain(),
                        new CollectorRun().withTimeout(3)
                    ),
                    //align(),
                    new RapidFire().withTimeout(3)
                ),
                new RevFlywheel(Constants.Shooter.RPM_FAR_COURT)
            ),

            new PrintCommand("Auto: Mobility Ended")
        );
    }
}
