package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class setShooterRpmCommand extends CommandBase {
    public setShooterRpmCommand(){}
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // ts: getEntry RPM
        Robot.shooterRpm = Robot.shooterRpmGetEntry.getDouble(Robot.shooterRpm);
        Robot.shooterRpmSync = Robot.shooterRpm;
        Robot.shooterRpmGetEntry.setDouble(Robot.shooterRpmSync);
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
