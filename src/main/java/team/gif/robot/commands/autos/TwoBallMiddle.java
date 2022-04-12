package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.autoaim.LimelightAutoAim;
import team.gif.robot.commands.collector.CollectorDown;
import team.gif.robot.commands.collector.CollectorRun;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RapidFire;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class TwoBallMiddle extends SequentialCommandGroup {

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

    public TwoBallMiddle() {
        addCommands(
            new ParallelDeadlineGroup(
                reverse(),
                new CollectorDown(),
                new CollectorRun(),
                new HoodUp()
            ),
            new ParallelDeadlineGroup(
                new CollectorRun().withTimeout(2),
                reverseAgain(),
                new RevFlywheel(Constants.Shooter.RPM_AUTO_UPPER_HUB)
            ),
            new ParallelDeadlineGroup(
                new LimelightAutoAim().withTimeout(4), // If limelight is not functioning, this will end immediately
                new RevFlywheel(Constants.Shooter.RPM_AUTO_UPPER_HUB)
            ),

            // This is the backup action in case the limelight isn't working
            new WaitUntilCommand(Robot.limelightaim::noTarget),
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_AUTO_UPPER_HUB).withTimeout(2),
                new RapidFire()
            )
        );
    }
}
