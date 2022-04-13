package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.*;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.autoaim.LimelightAutoAim;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.commands.limelight.LimelightBallDetection;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class BallFollowTest extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-10.0, -2.0, -10.0)
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(8.0, -4.0, 0.0)
            ),
            RobotTrajectory.getInstance().configForwardSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public BallFollowTest() {
        addCommands(
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorDown()
            ),
            new ParallelDeadlineGroup(
                new LimelightBallDetection(),
                new CollectorRun()
            ),
            new CollectorRun().withTimeout(2),
            new ResetHeading(),
            new ParallelDeadlineGroup(
                forward(),
                new WaitCommand(0.5).andThen(new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB))
            ),
            new ParallelDeadlineGroup(
                new LimelightAutoAim(),
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB),
                new RapidFire()
        )
        );
    }
}
