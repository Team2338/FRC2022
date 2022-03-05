package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorUp extends CommandBase {

    public CollectorUp() {
        addRequirements(Robot.collectorPneumatics);
    }

    @Override
    public void initialize() {
        Robot.collectorPneumatics.raise();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
