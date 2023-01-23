package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import team.gif.lib.DriveUntilAngle;
import team.gif.lib.DriveUntilLevel;

import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.commands.drivetrain.ResetHeading;
import team.gif.lib.SleepMe;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.commands.shooter.RevFlywheel;

import java.util.List;

public class Mobility extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-15.0, 0.0, 0.0)
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
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-15.0, 0.0, 0.0)
            ),
            RobotTrajectory.getInstance().configReverseSuperSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forwardInit() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(0, 0.0, 0.0),
                        new Pose2dFeet().set(8.0, 0.0, 0.0)
                ),
                RobotTrajectory.getInstance().configForward
        );
        // Create the command using the trajectory
        
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(8.0, 0.0, 0.0),
                new Pose2dFeet().set(15.0, 0.0, 0.0)
            ),
            RobotTrajectory.getInstance().configForwardSuperSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    //                new WaitCommand(0.5).andThen(new RevFlywheel(Constants.Shooter.RPM_AUTO_5_BALL_UPPER_HUB))
    // new WaitCommand(3),
    public Mobility() {

            addCommands(
                    forwardInit(),

            new WaitCommand(0.25),
            new ParallelDeadlineGroup(
                new DriveUntilLevel(),
                forward()
            )
//             new ParallelDeadlineGroup(
//                new DriveUntilAngle(),
//                reverse()
//            ),
//            new ResetHeading(),
//            new WaitCommand(0.5),
//            new ParallelDeadlineGroup(
//                new DriveUntilLevel(),
//                forward()
//            ),
//            new ParallelDeadlineGroup(
//                new DriveUntilLevel(),
//                reverseAgain()
            //)
        );
    }
}
