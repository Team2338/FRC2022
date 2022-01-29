package team.gif.robot.commands.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Intake;

public class IntakeRun extends CommandBase
{
    public IntakeRun(){
        super();
        addRequirements(Robot.intake);
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.


    public void execute() {
        Robot.intake.setSpeed(0.75);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.setSpeed(0);
    }


}








