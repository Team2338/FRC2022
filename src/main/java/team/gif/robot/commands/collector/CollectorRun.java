package team.gif.robot.commands.collector;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class CollectorRun extends CommandBase
{
    public CollectorRun(){
        super();
        addRequirements(Robot.collector);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Globals.isCollecting = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    public void execute() {
        Robot.collector.setSpeedPercent(0.6);
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
        Robot.collector.setSpeedPercent(0);
    }
}








