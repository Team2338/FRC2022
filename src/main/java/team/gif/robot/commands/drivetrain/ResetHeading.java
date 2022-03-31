package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.drivers.Pigeon;

public class ResetHeading extends CommandBase {

    public ResetHeading() {
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Pigeon.getInstance().resetPigeonPosition();
        Robot.drivetrain.resetEncoders();
        Robot.drivetrain.resetPose();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
