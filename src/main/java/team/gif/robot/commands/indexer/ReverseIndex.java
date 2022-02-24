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
        Robot.collector.setSpeedPercent(-0.8);
        Robot.collector.getEntrySensor(); // Tharun: TODO: I don't know what goes under the parentheses
        Robot.indexer.setMidMotorSpeed(-0.6);
        Robot.indexer.setBeltMotorSpeedPercent(-0.6);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.collector.setSpeedPercent(0);
        Robot.collector.getEntrySensor(); // Tharun: something as the line #21
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Globals.indexerEnabled = true;
    }
}
