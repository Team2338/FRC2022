package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Globals;
import team.gif.robot.Robot;

public class RapidFire extends CommandBase {

    public RapidFire() {
        super();
        addRequirements(Robot.indexer); // this might be another way to take control of the indexer. Test this out
    }

    @Override
    public void initialize() {
        //Globals.indexerEnabled = false;
    }

    @Override
    public void execute() {
        Robot.limelight.setLEDMode(3);

        if ( Robot.shooter.isInTolerance() ) {
            Robot.indexer.setBeltMotorSpeedPercent(1.0);
            Robot.indexer.setMidMotorSpeed(1.0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.indexer.setBeltMotorSpeedPercent(0);
        Robot.indexer.setMidMotorSpeed(0);

        //Globals.indexerEnabled = true;
    }
}
