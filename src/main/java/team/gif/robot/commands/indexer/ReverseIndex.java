package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class ReverseIndex extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    public ReverseIndex() {
        addRequirements(Robot.indexer, Robot.collector);
    }

    @Override
    public void initialize() {
        Globals.indexerEnabled = false; //TODO: Do we really need this anymore?
    }

    @Override
    public void execute() {
        Robot.collector.setSpeedPercent(-0.5);
        Robot.indexer.setStageMotorSpeed(-0.6);
        Robot.indexer.setBeltMotorSpeed(-0.5);
    }

    @Override
    public void end(boolean interrupted) {
        Robot.collector.setSpeedPercent(0);
        Robot.indexer.setBeltMotorSpeed(0);
        Robot.indexer.setStageMotorSpeed(0);
        Globals.indexerEnabled = true;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
