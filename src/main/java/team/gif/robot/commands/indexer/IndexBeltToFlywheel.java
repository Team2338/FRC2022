package team.gif.robot.commands.indexer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class IndexBeltToFlywheel extends CommandBase {
    double initTime = 0;
    public IndexBeltToFlywheel() {
        super();
        addRequirements(Robot.indexer);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        initTime = Timer.getFPGATimestamp();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.indexer.setBeltMotorSpeed(1);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - initTime > 2;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeed(0);
    }
}
