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

public class TwoBallRight extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-3.4, 0.0, 0.0) //4.5 original
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public TwoBallRight() {

        addCommands(
            new CollectorDown(),
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorRun(),
                new HoodUp(),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB).withTimeout(4),
                new CollectorRun().withTimeout(1),
                new RapidFire()
            )
        );
    }
}
