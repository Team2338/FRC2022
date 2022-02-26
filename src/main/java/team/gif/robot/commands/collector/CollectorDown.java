package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorDown extends CommandBase {

    public CollectorDown() {
        //addRequirements(intake);
    }

    @Override
    public void initialize() {
        Robot.collectorPneumatic.lower();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() { return false; }

    @Override
    public void end(boolean interrupted) {
    }
}
