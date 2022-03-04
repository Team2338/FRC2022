package team.gif.robot.commands.collector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Robot;

public class CollectorDown extends CommandBase {

    public CollectorDown() {
        //-addRequirements(Robot.collectorPneumatics);
        System.out.println("Command Constructor");
    }

    @Override
    public void initialize() {
        //-Robot.collectorPneumatics.lower();
        System.out.println("Command Init");
    }

    @Override
    public void execute() {
        System.out.println("Command Execute");
    }

    @Override
    public boolean isFinished() {
        System.out.println("Command isFinished false");
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Command end");
    }
}
