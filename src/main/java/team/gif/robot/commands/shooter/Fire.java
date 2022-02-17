package team.gif.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Indexer;

public class Fire extends CommandBase {

    public Fire() {
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Robot.limelight.setLEDMode(3);
        // Fire is used in auto but OI isn't instantiated yet so need to check first
        double speed = (Robot.oi != null && (Robot.oi.dStart.get() || Robot.oi.aDPadRight.get())) ? Constants.Shooter.RPM_HIGH : Constants.Shooter.RPM_LOW;
        if ( ( Robot.shooter.getSpeed() > (speed - 20.0) )
                && (Robot.indexer.getSensorBelt())) {

//            System.out.println("Firing speed " + Robot.shooter.getVelocity());
            Robot.indexer.setBeltMotorSpeed(0.5);
            // The indexer will move the remaining power cells forward. No need to move them here.
        } else {
            Robot.indexer.setBeltMotorSpeed(0);
        }
    }


    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.limelight.setLEDMode(1);
        Indexer.getInstance().setSpeedFive(0);
    }
}
