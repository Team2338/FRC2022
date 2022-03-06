package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorDown extends CommandBase {

    private int count;
    private boolean isDone;

    public CollectorDown() {
        addRequirements(Robot.collectorPneumatics);
    }

    @Override
    public void initialize() {
        Robot.collectorPneumatics.collectorLower();
        count = 0;
        isDone = false;
    }

    @Override
    public void execute() {
        if(++count >= 30) { // 0.5 second delay
            Robot.collectorPneumatics.entryLower();
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
