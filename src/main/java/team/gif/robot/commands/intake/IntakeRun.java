package team.gif.robot.commands.intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

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
        if ((Robot.indexer.sensorStates()[0] && Robot.indexer.sensorStates()[1]) || (Robot.indexer.sensorStates()[1] && Robot.indexer.sensorStates()[2])) {
            Robot.intake.setSpeedPercent(0);
            System.out.println("Robot Full");
        } else {
            Robot.intake.setSpeedPercent(0.75);
        }
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
        Robot.intake.setSpeedPercent(0);
    }


}








