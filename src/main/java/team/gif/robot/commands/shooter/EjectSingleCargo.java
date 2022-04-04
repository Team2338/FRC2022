package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class EjectSingleCargo extends CommandBase {

    private int counter;

    public EjectSingleCargo() {
        super();
        addRequirements(Robot.shooter);
    }

    @Override
    public void initialize() {
        // Turn off indexer with flag (doesn't kill indexer, simply disables it during Eject
        Globals.indexerEnabled = false;
        counter=0;
        Robot.hood.setHoodUp();
        Robot.shooter.setSpeedPID(Constants.Shooter.RPM_FENDER_LOWER_HUB_BLOCKED);
    }

    @Override
    public void execute() {
        if (Robot.shooter.isInTolerance()) {
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            Robot.indexer.setMidMotorSpeed(1.0);
            counter++;
        }
    }

    @Override
    public boolean isFinished() {
        // Only run if the shooter is running
        // Without this, the belt runs regardless of the shooter state and
        // could possibly jam a ball between the running belt and the stopped shooter.
        return !Globals.shooterRunning || (counter > 50); // only run for 1 second to eject ball
    }

    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);
        Globals.indexerEnabled = true;
    }
}
