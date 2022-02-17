package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Shooter;

public class RevFlywheel extends CommandBase {

    public RevFlywheel() {
        Robot.limelight.setLEDMode(3);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.limelight.setLEDMode(3);//force on
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.limelight.setLEDMode(3);
        // RevFlyWheel is used in auto but OI isn't instantiated yet so need to check first
        if (Robot.oi != null && (Robot.oi.dStart.get() || Robot.oi.aDPadRight.get())) {
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_HIGH);
        } else {
            Robot.shooter.setSpeedPID(Constants.Shooter.RPM_LOW);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() { return false; }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.shooter.setSpeedPercent(0);
        Robot.limelight.setLEDMode(1);//force off
    }

}