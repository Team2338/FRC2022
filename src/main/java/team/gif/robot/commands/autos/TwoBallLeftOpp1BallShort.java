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

public class TwoBallLeftOpp1BallShort extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(0.0, 0.0, 0.0),
                new Pose2dFeet().set(-4.0, 0.0, 0.0)
            ),
        RobotTrajectory.getInstance().configReverseSuperSlow
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

    public Command shootingLocation() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-4.25, 3.5, 90.0),
                new Pose2dFeet().set(-6.0,0,165) // change values to turn and shoot
            ),
            RobotTrajectory.getInstance().configForward
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command startingLocation() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                new Pose2dFeet().set(-6.0, 0, 165),
                new Pose2dFeet().set(-4.0,-3,-60)
            ),
            RobotTrajectory.getInstance().configReverseSlow
        );
        // Create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }


    public TwoBallLeftOpp1BallShort() {
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
                new CollectorRun(),
                new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB-400)
            ),
            new ParallelDeadlineGroup(
                oppOneBall(),
            new CollectorRun()
            ),
            new CollectorRun().withTimeout(0.5),
            shootingLocation(), //position to shoot ball away
            new ParallelDeadlineGroup(
                new RapidFire().withTimeout(3),
                new RevFlywheel(Constants.Shooter.RPM_EJECT_OPP_BALL + 550)
            )
        );
    }
}
