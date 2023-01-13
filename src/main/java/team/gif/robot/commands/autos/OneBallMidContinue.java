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
import team.gif.robot.commands.autoaim.LimelightAutoAim;
import team.gif.robot.commands.hood.HoodUp;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class OneBallMidContinue extends SequentialCommandGroup {

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

    public Command reverseAgain() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-4.5, 0.0, 0.0),
                        new Pose2dFeet().set(-16.0, 0.0, 0.0)
                ),
                RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public OneBallMidContinue() {

        addCommands(
            new ParallelDeadlineGroup(
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB - 200).withTimeout(2),
                reverse(),
                new HoodUp()
            ),
            new ParallelDeadlineGroup(
                new LimelightAutoAim().withTimeout(2), // If limelight is not functioning, this will end immediately
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB - 200)
            ),
            reverseAgain()
        );
    }
}
