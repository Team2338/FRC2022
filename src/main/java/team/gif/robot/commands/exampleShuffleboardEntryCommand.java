package team.gif.robot.commands;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class exampleShuffleboardEntryCommand extends CommandBase {
    public exampleShuffleboardEntryCommand(){}

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // T.S: we need to get value from the getEntry and set it to inputValue
        Robot.exampleShuffleboardValue = Robot.exampleShuffleboardEntry.getDouble(Robot.exampleShuffleboardValue);
        Robot.exampleShuffleboardEntry.setDouble(Robot.exampleShuffleboardValue);
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
