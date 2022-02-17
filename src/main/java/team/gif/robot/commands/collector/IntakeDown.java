package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class IntakeDown extends CommandBase {

    public IntakeDown() {
        //addRequirements(intake);
    }

    @Override
    public void initialize() {
        Robot.collector.setSolenoid(true);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() { return true; }
}
