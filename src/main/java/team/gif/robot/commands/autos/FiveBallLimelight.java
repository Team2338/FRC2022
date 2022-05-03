package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.autoaim.LimelightAutoAim;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.collector.CollectorUp;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.limelight.LimelightBallDetection;
import team.gif.robot.commands.limelight.LimelightHubDetection;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class FiveBallLimelight extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-3.4, 0.0, 0.0) // 1st cargo location
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command pickUpNext() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-3.4, 0.0, 0.0),
                new Pose2dFeet().set(-3.4, 2.0, 127.0), // ~turn in place
                new Pose2dFeet().set(-1.7, 7.0, 90.0), // 2nd cargo location
                new Pose2dFeet().set(-1.7, 8.0, 90.0), // extend a foot to make sure
                new Pose2dFeet().set(-2.5, 12.0, 48.0) // shooting location
            ),
            RobotTrajectory.getInstance().configReverseMedium5Ball
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command pickupTerminalClose() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-2.5, 12.0, 48.0),
                new Pose2dFeet().set(-4.0, 19, 22.0) // close to 3rd cargo (terminal) location
            ),
            RobotTrajectory.getInstance().configReverseFast
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-4.7, 20.3, 22.0),
                new Pose2dFeet().set(-2.5, 12.0, 48.0) // shooting location
            ),
            RobotTrajectory.getInstance().configForward5BallFast
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }


    public FiveBallLimelight() {
        addCommands(
            new CollectorDown(),
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorRun(),
                new HoodUp(),
                new RevFlywheel(Constants.Shooter.RPM_AUTO_RIGHT_RING)
            ),
            new ParallelDeadlineGroup( // Only allow enough time to shoot first cargo. Shoot second cargo next.
                new RevFlywheel(Constants.Shooter.RPM_AUTO_RIGHT_RING).withTimeout(1),
                new CollectorRun().withTimeout(0.8).andThen(new CollectorUp()),
                new RapidFire()
            ),
            new ParallelDeadlineGroup(
                pickUpNext(),
                new WaitCommand(0.75).andThen(new CollectorDown()),
                new WaitCommand(1.0).andThen(new CollectorRun()),
                new WaitCommand(1.25).andThen(new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB))
            ),
            new ParallelDeadlineGroup( // Allow enough time for both cargo to shoot
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB).withTimeout(1.5),
                new HoodUp(),
                new CollectorRun().withTimeout(0.5),
                new RapidFire()
            ),
            pickupTerminalClose(), // Get close to the cargo at the terminal
            new ParallelDeadlineGroup(
                new LimelightBallDetection(), // Use limelight to get us the rest of the way
                new CollectorRun()
            ),
            new CollectorRun().withTimeout(0.4), // Give robot time to collect cargo
            new ParallelDeadlineGroup(
                new LimelightHubDetection().withTimeout(0.4),  // Drive away from terminal 2 feetish
                new CollectorRun()
                //new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_LL_UPPER_HUB)
            ),
            new CollectorRun().withTimeout(1.0), // Give HP time to feed cargo
            new ParallelDeadlineGroup(
                new LimelightHubDetection(),  // Drive to the hub using limelight
                new CollectorRun().withTimeout(1.3),
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_LL_UPPER_HUB)
            ),
            new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_LL_UPPER_HUB).withTimeout(0.5), // Sit at location to settle bot
            new ParallelDeadlineGroup(
                new LimelightAutoAim(), // Re-align with limelight. If limelight is not functioning, this will end immediately
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_LL_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_LL_UPPER_HUB),
                new RapidFire()
            )
        );
    }
}
