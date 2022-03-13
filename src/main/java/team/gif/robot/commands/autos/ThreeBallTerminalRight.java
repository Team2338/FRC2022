package team.gif.robot.commands.autos;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.*;
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

public class ThreeBallTerminalRight extends SequentialCommandGroup {

    public Command reverse() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(0.0, 0.0, 0.0),
                        new Pose2dFeet().set(-3.4, 0.0, 0.0) // 1st cargo location
                ),
                RobotTrajectory.getInstance().configReverseSlow
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command pickUpNext2() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-3.4, 0.0, 0.0),
                        new Pose2dFeet().set(-3.4, 2.0, 95.0), // ~turn in place
                        new Pose2dFeet().set(-5.0, 8.0, 70.0), // waypoint to drive past cargo
                        new Pose2dFeet().set(-5.5,20.5,54.0) // 2nd cargo (terminal) location
                ),
                RobotTrajectory.getInstance().configReverse
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Command forward() {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
                List.of(
                        new Pose2dFeet().set(-5.5, 20.5, 54.0),
                        new Pose2dFeet().set(4.0, 11.0, 85.0) // shooting location
                ),
                RobotTrajectory.getInstance().configForwardFast
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public ThreeBallTerminalRight() {

        addCommands(
                new CollectorDown(),
                new ParallelDeadlineGroup(
                        reverse(),
                        new CollectorRun(),
                        new HoodUp(),
                        new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB)
                ),
                new ParallelDeadlineGroup(
                        new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB).withTimeout(1.5),
                        new CollectorRun().withTimeout(1).andThen(new CollectorUp()),
                        new RapidFire()
                ),
                new ParallelDeadlineGroup(
                        pickUpNext2(),
                        new WaitCommand(1).andThen(new CollectorDown()),
                        new WaitCommand(2).andThen(new CollectorRun())
                ),
                new ParallelDeadlineGroup(
                        forward(),
                        new CollectorRun().withTimeout(2),
                        new WaitCommand(3).andThen( new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB))
                ),
                new ParallelDeadlineGroup(
                        new RevFlywheel(Constants.Shooter.RPM_RING_UPPER_HUB),
                        new RapidFire()
                )
        );
    }
}
