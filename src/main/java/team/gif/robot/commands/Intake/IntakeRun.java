package team.gif.robot.commands.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Intake;

public class IntakeRun extends CommandBase
{


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.


    public void execute() {
        //if (!Indexer.getInstance().getState()[1] || !Indexer.getInstance().getState()[2])
        //} else {
        Robot.intake.setSpeed(0.75);
        }



    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.setSpeed(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}








