package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorUp extends CommandBase {

    private int count;
    private boolean isDone;

    public CollectorUp() {
        addRequirements(Robot.collectorPneumatics);
    }

    @Override
    public void initialize() {
        Robot.collectorPneumatics.entryRaise();
        count = 0;
    }

    @Override
    public void execute() {
        if(++count >= 100) {
            Robot.collectorPneumatics.collectorRaise();
            isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
