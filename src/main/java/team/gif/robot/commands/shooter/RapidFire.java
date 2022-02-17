package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Indexer;
import team.gif.robot.subsystems.Collector;

public class RapidFire extends CommandBase {

    public RapidFire() {
    }

    @Override
    public void initialize() {
        Globals.indexerEnabled = false;
    }

    @Override
    public void execute() {
        Robot.limelight.setLEDMode(3);

        boolean isFarShot = Robot.oi != null && (Robot.oi.dStart.get() || Robot.oi.aDPadRight.get());
        double speed = isFarShot ? Constants.Shooter.RPM_HIGH : Constants.Shooter.RPM_LOW;

        if ( ( Robot.shooter.getSpeed() > (speed - 20.0) )) {
            Robot.indexer.setBeltMotorSpeed(0.5);
            Robot.indexer.setStageMotorSpeed(0.4);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeed(0);
        Robot.indexer.setStageMotorSpeed(0);

        Globals.indexerEnabled = true;
    }
}
