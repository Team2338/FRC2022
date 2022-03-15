package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.lib.LimelightLedMode;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class RapidFire extends CommandBase {

    public RapidFire() {
        super();
        // can't use addRequirements here because in auto, once rapid fire runs, it will
        // take over the indexer, and then the indexer doesn't run again
        //addRequirements(Robot.indexer);
    }

    @Override
    public void initialize() {
        // Turn off indexer with flag (doesn't kill indexer, simply disables it during RapidFire
        Globals.indexerEnabled = false;
    }

    @Override
    public void execute() {
        Robot.limelight.setLEDMode(LimelightLedMode.ON);

        if (Robot.shooter.isInTolerance()) {
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            Robot.indexer.setMidMotorSpeed(1.0);
        }
    }

    @Override
    public boolean isFinished() {
        // only run if the shooter is running
        // without this, the belt runs regardless of the shooter state and
        // could possibly jam a ball between the running belt
        // and the stopped shooter.
        return !Globals.shooterRunning;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Globals.indexerEnabled = true;
    }
}
