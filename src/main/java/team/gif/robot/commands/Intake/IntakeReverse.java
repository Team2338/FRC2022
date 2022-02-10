package team.gif.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class IntakeReverse extends CommandBase
{
    public IntakeReverse(){
        super();
        addRequirements(Robot.intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.intake.setSpeedPercent(-0.5);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.setSpeedPercent(0);
    }

}

