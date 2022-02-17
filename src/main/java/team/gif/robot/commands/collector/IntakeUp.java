package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Collector;

public class IntakeUp extends CommandBase {

    public IntakeUp() {
        //addRequirements(intake);
    }

    @Override
    public void initialize() {
        Robot.collector.setSolenoid(false);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() { return false; }
}
