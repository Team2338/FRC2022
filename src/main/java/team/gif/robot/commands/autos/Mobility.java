package team.gif.robot.commands.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import team.gif.lib.Pose2dFeet;
import team.gif.lib.RobotTrajectory;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;
import java.util.List;

public class Mobility extends SequentialCommandGroup {

    public Command reverse () {
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            List.of(
                    new Pose2dFeet().set(0.0, 0.0, 0.0),
                    new Pose2dFeet().set(-3.0, 0.0, 0.0)
                //new Pose2d(Units.feetToMeters(0.0), 0, new Rotation2d(0)),
                //new Pose2d(Units.feetToMeters(-3.0), 0, new Rotation2d(0))
            ),
            RobotTrajectory.getInstance().configReverse
        );
        // create the command using the trajectory
        RamseteCommand rc = RobotTrajectory.getInstance().createRamseteCommand(trajectory);
        // Run path following command, then stop at the end.
        return rc.andThen(() -> Robot.drivetrain.tankDriveVolts(0, 0));
    }

    public Mobility() {
        System.out.println("Auto: Mobility Selected");

        addCommands(
            new PrintCommand("Auto: Mobility Started"),
            reverse(),
            new PrintCommand("Auto: Mobility Ended")
        );
    }
}
