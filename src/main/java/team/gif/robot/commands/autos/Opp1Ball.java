package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.collector.CollectorUp;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class Opp1Ball extends SequentialCommandGroup {

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

  public Command shootBall() {
    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
        List.of(
            new Pose2dFeet().set(-4.25, 3.5, 90.0),
            new Pose2dFeet().set(-6.0,0,137) // change values to turn and shoot
        ),
        RobotTrajectory.getInstance().configForward
    );
    // Create the command using the trajectory
    RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
    // Run path following command, then stop at the end.
    return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
  }



  public Opp1Ball() {
    addCommands(
        new ParallelDeadlineGroup(
            new CollectorRun().withTimeout(2),
            new CollectorDown(),
            reverse(),
            new HoodUp(),
            new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB-400)
        ),
        new ParallelDeadlineGroup(
            new RapidFire().withTimeout(1.4),
            new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB-400)
        ),
        new ParallelDeadlineGroup(
            oppOneBall(),
            new CollectorRun()
        ),
        new CollectorRun().withTimeout(0.5),
        new ParallelDeadlineGroup(
            shootBall(), //position to shoot ball away
            new CollectorRun()
        ),
        new ParallelDeadlineGroup(
            new RapidFire(),
            new RevFlywheel(Constants.Shooter.RPM_FENDER_LOWER_HUB_BLOCKED)
        )
    );
  }
}
