package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class ReverseIndex extends CommandBase {
    public ReverseIndex() {
        addRequirements(Robot.indexer, Robot.collector);
    }

    @Override
    public void initialize() {
        Globals.indexerEnabled = false; // TODO: Do we really need this anymore?
    }

    @Override
    public void execute() {
        Robot.collector.setSpeedPercentCollector(-0.8);
        Robot.collector.setSpeedPercentEntry(-0.8);
        Robot.indexer.setMidMotorSpeed(-0.6);
        Robot.indexer.setBeltMotorSpeedPercent(-0.6);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.collector.setSpeedPercentCollector(0);
        Robot.collector.setSpeedPercentEntry(0);
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Globals.indexerEnabled = true;
    }
}
